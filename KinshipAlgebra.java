/* history
* 5/29 DR added "progress.addElement(gen);" to if clause: "if (theProd.size() == 0)"
* in procedure checkRecursivePath
* 5/31 DR rewrote checkRecursivePath to search for term product loops outward with gen
* and back with genr so that equations of form CCPP=CP can be found; added other conditions
* that must be satisfied prior to searching for loops
* all loops in the form of equations returned in a single vector
* 7/15 DR replace getElement with findElement in checkRecursivePath; added checks in case a
* null element is returned
* 10/15 DR change <= to < in for(int i=0;i<ndx;i++)//should this be i ,ndx or i<=ndx?
* in checkRecursivePath; this may be a problem but it works for Shipibo
* 11/11 move all Arrow procedures to Algebra
* 11/10/02 DR added procedure checkParentSibPath(String prod, String gen, TransferKinInfoVector kv)
*/


public class KinshipAlgebra {

	/** Determines if it is possible to begin building an Algebra
	* Based on properties of generators and orientations
	* If there is at most one UP generator and at most one SIDE
	* generator then returns true;
	* @return true if possible to begin building Algebra
	*/

	TransferKinInfoVector kv=null;
	MakeAlgebra makeAlg=new MakeAlgebra(this);

	public KinshipAlgebra(TransferKinInfoVector k) {
		kv = k;
	}

	/** checks to see if the base generators are present in the kinterm map
	*	and establishes these in the algebra via MakeAlgebra if necessary
	* @see MakeAlgebra
	* @ return true if base generators established
	* does not check to see if this step is needed or has already been done
	*/

	public boolean establishBaseGenerators() {
		String arrow=null;
		System.out.println(" IN KINSHIP ALGEBRA BASE GENS");
		if (kv.checkSimplicityOfStructure()){
			if (kv.up == 1) {
				arrow = kv.getEffectiveGenerators(kv.UP).getSymbol(0).getTerm();
				makeAlg.addGenerator(arrow);
			} else // erase following after checkout!!!!
				Debug.prout(5,"KinshipAlgebra.establishBaseGenerators: up not satisfied");
			if (kv.left == 1) {
				arrow = kv.getEffectiveGenerators(kv.LEFT).getSymbol(0).getTerm();
				makeAlg.addGenerator(arrow);
			} else if (kv.right == 1) {
				arrow = kv.getEffectiveGenerators(kv.RIGHT).getSymbol(0).getTerm();
				makeAlg.addGenerator(arrow);
			}
			establishIdentityTerm();
			if (!establishReciprocalsForGenerators()) {
				Debug.prout(1,"Need to do something in KinshipAlgebra.establishBaseGenerators relating"+
				" to establishReciprocalsForGenerators()");
			}
			return true;
		} else {
			for(int i=0;i< kv.getEffectiveGenerators(kv.UP).size();i++) {
				arrow = kv.getEffectiveGenerators(kv.UP).getSymbol(i).getTerm();
				makeAlg.addGenerator(arrow);
			}
			establishIdentityTerm();
			establishReciprocalsForGenerators();
			return false;
		}

		//} else return false;
	}

	public boolean establishReciprocalsForGenerators() {
		return makeAlg.establishReciprocalsForGenerators();
	}
	// ***********************************
	// need routine to find a reciproal in the kin term map and return
	// reciproal and equation that made it.
	public ListVector checkGeneratorForReciprocal(String gen) {
		StringVector ft = getFocalTerms();
		ListVector ret = new ListVector();
		if (ft.size() < 1) return ret;
		//	System.out.println("checkGeneratorForReciprocal: focal terms "+ft.toString());
		for(ft.reset();ft.isNext();) {
			ListVector eq = kv.findReciprocals(gen,ft.getNext());
			ret.append(eq);
		}
		if (ret.size() > 1) {
			// ++++ Message seems too many reciprocals whatever
			Debug.prout(2,"checkGeneratorForReciprocal: Message too many reciprocals for "+gen);

		}
		return ret;
	}

	public boolean establishIdentityTerm() {
	    if (makeAlg.hasIdentityElement()) return true;
	    String id = getIdentityTerm();
	    if (id != null) {
		    return (makeAlg.addIdentityElement(id));
	    } else {
		Debug.prout(5,"Message - you need to add an identity term if applicable ++++");
		return false;
	    }
	}

	public StringVector getFocalTerms() {
		return kv.getFocalTerms();
	}

	public boolean isFocalTerm(String term) {
		return getFocalTerms().indexOf(term) != -1;
	}


	/** finds the identity element, if any
	* @return the identity element if found, null if not
	*/
	public String getIdentityTerm() {
	    String ret = null;
		for (int i = 0;i < kv.size(); i++){
			String x;
			//if (isIdentityTerm(x = kv.getNext().getEffectiveTerm())) {
			if (kv.isIdentityTerm(x = ((TransferKinInfo)kv.elementAt(i)).getEffectiveTerm())) {
				if (ret != null) {
					// message about having two identity elements ++++
					// suggest running TransferKinInfoVector.findstructuralequivalence
					Debug.prout(0,"1: Message too many identity terms including "+x+" and "+ret);
				} else ret = x;
			}
		}
	    return ret;
	}

	public boolean addIdentityTerm() {
	    String id=null;
	    if ((id = getIdentityTerm()) != null) {
		return makeAlg.addIdentityElement(id);
	    }
	    return true;
	}

	/** returns lhs and rhs of recursive clip equation as StringVectors
	* starts implicitly from a focal term
	* @param gen the generator to follow
	* @return The lhs and rhs of recursive clip equation as StringVectors
	**/

	ListVector checkRecursivePath(String gen) {
	    return checkRecursivePath(gen,gen);
	}


	/** returns lhs and rhs of recursive clip equation as StringVectors
	* @param prod The term to start from
	* @param gen the generator to follow
	* @return The lhs and rhs of recursive clip equation as StringVectors
	**/

	ListVector checkRecursivePath(String prod, String gen) {
		return checkRecursivePath(prod,gen,kv);
	}

	/** returns lhs and rhs of recursive clip equation as StringVectors
	* @param prod The term to start from
	* @param gen the generator to follow
	* @param kv the TransferKinInfoVector to use
	* @return The lhs and rhs of recursive clip equation as StringVectors
	**/
	ListVector checkRecursivePath(String prod, String gen, TransferKinInfoVector kv) {
		Debug.prout(6,"checkRecursivePath :"+gen);
		ListVector ret = new ListVector();
		StringVector theSide = new StringVector();
		StringVector progress = new StringVector();
		progress.addElement(gen);
		StringVector theProd=null;
		TransferKinInfo kk;
		int istop = 0;
		String oldProd = prod;
		String genr = "";
		boolean focalFlag = false;

//	System.out.println("PROD "+prod+" gen "+gen+" alg gen = "+makeAlg.getAlgName(gen));
//	System.out.println(" stuff "+makeAlg.theAlgebra.getElement(makeAlg.getAlgName(gen)).getReciprocal());
        String st = makeAlg.getAlgName(gen);
//        System.out.println(" GEN "+gen+"st= "+st+" GenYYYYYY "+makeAlg.theAlgebra.getElements());
        //AlgebraSymbol a1 = makeAlg.theAlgebra.getElement(st);
        AlgebraSymbol a1 = makeAlg.theAlgebra.findElement(st);

        //System.out.println("GenZZZZZZ "+makeAlg.theAlgebra.getElements());
        //AlgebraSymbol as = new AlgebraSymbol();
        AlgebraSymbol as = null;
        if (a1 != null)
            as = a1.getReciprocal();

      //  AlgebraSymbol as = makeAlg.theAlgebra.getElement(makeAlg.getAlgName(gen)).getReciprocal();
//System.out.println("st "+st+" a1 "+a1+" as "+as+" genr "+genr+" gen "+gen+" ep ");
        if (as != null){
		    genr = makeAlg.getKinName(as.getValue());
			if (genr == null) return ret;//added 2/27/05 dwr
		//	if (genr == null) {genr = "";as = null;}//genr is kin name of as; don't proceed if genr does not exist
//System.out.println("st "+st+" a1 "+a1+" as "+as+" genr "+genr+" gen "+gen+" ep ");
		    if (genr!= "") focalFlag = isFocalTerm((String)kv.getEffectiveProducts(gen,genr).elementAt(0));
		}

		for(;;) {
			if (( kk = kv.lookupTerm(prod)) == null) {
				throw new KintermMapException(44,"KinshipAlgebra: checkRecursivePath - lookupTerm failed to find term="+prod);
			}
			if ( kk.isEtc()) return ret; // don't limit path size based on kin term map
		    istop++;

		    if ((genr != "")&& !focalFlag) {
	            ListVector lv = _checkRecursivePath(istop,progress,oldProd,gen,genr,kv);
			if (lv.size() == 0) System.out.println(" ret "+ret);
				if (lv.size() > 0) {
	                ret.addElement(lv.elementAt(0));
	                ret.addElement(lv.elementAt(1));
	            }
	        }
			//System.out.println("WWWWWWWWWWWWWWW kv "+kv.lookupTerm(prod).getProducts());
			theProd = kv.getEffectiveProducts(prod,gen);
			//System.out.println("theProd :"+theProd.toString()+" prof "+prod +" gen "+gen);
		//	System.out.println("progress :"+progress.toString()+" ret "=ret);
			if (theProd.size() == 0) {
			    progress.addElement(gen);
			//System.out.println("NEW1 progress "+progress+ " gen "+gen);

				//for(int i=0;i<= progress.size();i++)
				for(int i=0;i<= istop;i++)
					theSide.addElement(gen);
				ret.addElement(theSide);
				ret.addElement(new StringVector()); // Empty list means null equation -- not a kin term
				Debug.prout(4,"YYYYYYYYYYYYYYYYYYYYYY ret "+ ret);
				return ret;
			} else if (theProd.size() > 1) {
				Debug.prout(4,"YYYYYYYYYYYYYYYYYYYYYY nnnnnnn ret "+ ret);
				return ret; // not very adequate ++++++
			}
			prod= theProd.getSymbol(0);
			int ndx;
			if ((ndx = progress.indexOf(prod)) != -1) {
//System.out.println(" progress "+progress+" prod "+prod  +" ndx "+ndx);
				//for(int i=0;i<= progress.size();i++)
				for(int i=0;i<= istop;i++)
					theSide.addElement(gen);
				ret.addElement(theSide);
				StringVector otherSide = new StringVector();
		        if ((genr != "")&& !focalFlag) {
				    int sum = 0;
				    int j = 2;
				    while (j+sum < ndx+1){
				        sum = j+sum;
				        j++;
				    }
				    j--;
                    if (sum != ndx){
                        for (int i = 0; i+sum < ndx;i++)
				            otherSide.addElement(genr);
				    }
				    for(int i=0;i<j;i++)
					    otherSide.addElement(gen);
    			}else{
				    for(int i=0;i<=ndx;i++)
					    otherSide.addElement(gen);
				}
				if (otherSide.size() > 0)
				    ret.addElement(otherSide);
				//System.out.println("XXXXXXXXXXXXXXXXXXXXXXXX ret"+ret);
			//	System.out.println("theside "+theSide+"othersie "+otherSide);
				return ret;
			}
			progress.addElement(prod);
			//System.out.println("NEW progress "+progress);
            oldProd = prod;
			// build up left hand side copy old lhs to rhs before product
		}
	}

	ListVector _checkRecursivePath(int istop, StringVector progress, String prod, String gen, String genr, TransferKinInfoVector kv) {
		Debug.prout(6,"checkRecursivePath :"+genr);
		ListVector ret = new ListVector();
		StringVector theSide = new StringVector();
		StringVector theProd=null;
		TransferKinInfo kk;
		for(int ii = 0;ii<istop;ii++) {
			if (( kk = kv.lookupTerm(prod)) == null) {
				throw new KintermMapException(44,"KinshipAlgebra: checkRecursivePath - lookupTerm failed to find term="+prod);
			}
			 if ( kk.isEtc()) return ret; // don't limit path size based on kin term map
			theProd = kv.getEffectiveProducts(prod,genr);
		//	System.out.println("theProd2 :"+theProd.toString()+" prod "+prod+" genr "+genr+" istop "+istop);
			if (theProd.size() == 0) {
				for (int j=0;j<istop-ii;j++){
					progress.addElement(null);
				}
				Message.create(-1,"Warning: +"+genr+" of "+prod+" not defined in kin term map.","Check kin term map for possible error.",null,90);	
				return ret;
			} else if (theProd.size() > 1) {
				return ret; // not very adequate ++++++
			}
			else if (isFocalTerm((String)theProd.elementAt(0))){
			    return ret;
			}
			prod= theProd.getSymbol(0);
		// System.out.println("prod 2" + prod);
			int ndx;
			if ((ndx = progress.indexOf(prod)) != -1) {
				//for(int i=0;i<= progress.size();i++)
				for (int i=0;i<=ii;i++)
				    theSide.addElement(genr);
				for (int i=0;i<istop;i++)
					theSide.addElement(gen);
				ret.addElement(theSide);
				StringVector otherSide = new StringVector();
 				int sum = 0;
				//int j = 1;
				int j = 2;
				while (j+sum < ndx    +1){
				    sum = j+sum;
				    j++;
				}
				//sum = j+sum;
				j--;
			//System.out.println("sum "+sum+" ndx "+ndx +" j "+j);
//				if (ndx+1 < sum+j) {
                if (sum != ndx){
//				    for (int i = 0; i+sum <= ndx+1; i++)
                    for (int i = 0; i+sum < ndx;i++)
				        otherSide.addElement(genr);
				}
			   // else j++;
				for(int i=0;i<j;i++)
					otherSide.addElement(gen);
			progress.addElement(prod);
	            ret.addElement(otherSide);
				return ret;
			}
			progress.addElement(prod);
			//System.out.println("progress 222 "+progress+" prod "+prod);
		}
		return ret;
	}


	/** returns lhs and rhs of recursive clip equation as StringVectors
	* starts implicitly from a focal term
	* @param gen the generator to follow
	* @return The lhs and rhs of recursive clip equation as StringVectors
	**/

	ListVector checkParentSibPath(String gen) {
	    return checkParentSibPath(gen,gen);
	}


	/** returns lhs and rhs of recursive clip equation as StringVectors
	* @param prod The term to start from
	* @param gen the generator to follow
	* @return The lhs and rhs of recursive clip equation as StringVectors
	**/

	ListVector checkParentSibPath(String prod, String gen) {
		return checkParentSibPath(prod,gen,kv);
	}


	/** returns lhs and rhs of "parent of sib" equation as StringVectors
	* @param prod The term to start from
	* @param gen the generator to follow
	* @param kv the TransferKinInfoVector to use
	* @return The lhs and rhs of recursive clip equation as StringVectors
	**/
	ListVector checkParentSibPath(String prod, String gen, TransferKinInfoVector kv) {//sib,pa
		Debug.prout(6,"checkParentSibPath :"+gen);
		ListVector ret = new ListVector();
		StringVector leftSide = new StringVector();
		StringVector rightSide = new StringVector();
		StringVector theProd=null;
		theProd = kv.getEffectiveProducts(gen,prod);
		if ((theProd != null) && (((String)theProd.elementAt(0)).equals(gen))){
		    leftSide.addElement(gen);//pa
		    leftSide.addElement(prod);//sib
		    ret.addElement(leftSide);
			rightSide.addElement(gen);//pa
			ret.addElement(rightSide);
		}
		return ret;
	}






/*	ListVector checkRecursivePathXX(String prod, String gen, TransferKinInfoVector kv) {
		Debug.prout(6,"checkRecursivePath :"+gen);
		ListVector ret = new ListVector();
		StringVector theSide = new StringVector();
		StringVector progress = new StringVector();
		//progress.addElement(gen);
		StringVector theProd=null;
		TransferKinInfo kk;
		for(;;) {
			if (( kk = kv.lookupTerm(prod)) == null) {
				throw new KintermMapException(44,"KinshipAlgebra: checkRecursivePath - lookupTerm failed to find term="+prod);
			}
			 if ( kk.isEtc()) return ret; // don't limit path size based on kin term map
			theProd = kv.getEffectiveProducts(prod,gen);
		//	System.out.println("theProd :"+theProd.toString());
			if (theProd.size() == 0) {
			    progress.addElement(gen);
				for(int i=0;i<= progress.size();i++)
					theSide.addElement(gen);
				ret.addElement(theSide);
				ret.addElement(new StringVector()); // Empty list means null equation -- not a kin term
				return ret;
			} else if (theProd.size() > 1) {
				return ret; // not very adequate ++++++
			}

			prod= theProd.getSymbol(0);
		// System.out.println("Made it to second side!!!");
			int ndx;
			if ((ndx = progress.indexOf(prod)) != -1) {
				for(int i=0;i<= progress.size();i++)
					theSide.addElement(gen);
				ret.addElement(theSide);
				StringVector otherSide = new StringVector();
				for(int i=0;i<=ndx;i++)
					otherSide.addElement(gen);
				ret.addElement(otherSide);
				return ret;
			}
			progress.addElement(prod);
			// build up left hand side copy old lhs to rhs before product
		}
	}

*/
	public void setKv(TransferKinInfoVector kv) {
		this.kv = kv;
	}

	public TransferKinInfoVector getKv() {
		return kv;
	}

	public AlgebraSymbolVector getAlgebraSymbols() {
		return makeAlg.theAlgebra.getElements();
	}
}

// ---------------- Commented out code to delete!!!!!!

/*	public AlgebraSymbolVector getUpArrowsx(){
	    return getUpOrDownArrowsx(Bops.UP);
	}

	public AlgebraSymbolVector getDownArrowsx(){
	 return getUpOrDownArrowsx(Bops.DOWN);
	}

	public AlgebraSymbolVector getUpOrDownArrowsx(int updown){

		AlgebraSymbolVector ups = new AlgebraSymbolVector(1);
		AlgebraSymbolVector av = makeAlg.theAlgebra.getGenerators();
		AlgebraSymbolVector fe = makeAlg.theAlgebra.getFocalElements();

		for (av.reset();av.isNext();) {
			AlgebraSymbol as = av.getNext();
			AlgebraSymbol rcp = as.getReciprocal();

			for(fe.reset();fe.isNext();) {
				AlgebraSymbol aft = fe.getNext();
				if (makeAlg.theAlgebra.isEquation(new Equation(rcp,aft).addLhs(as))) {
					if (!makeAlg.theAlgebra.isEquation(new Equation(as,aft).addLhs(rcp))) {
						if (updown == Bops.UP)ups.addToEnd(as);
						else if (updown == Bops.DOWN) ups.addToEnd(rcp);
						else System.out.println("KinshipAlgebra: getUpOrDownArrows - Unknown arrow type");
					}
				}
			}
		}
		return ups;
	}

	public AlgebraSymbolVector getSideArrowsx(){

		AlgebraSymbolVector ups = new AlgebraSymbolVector(1);
		AlgebraSymbolVector av = makeAlg.theAlgebra.getGenerators();

		for (av.reset();av.isNext();) {
			AlgebraSymbol as = av.getNext();
			    if ((Algebra.getCurrent().isArrow(as,Bops.RIGHT)) || (Algebra.getCurrent().isArrow(as,Bops.LEFT)))
			        ups.addToEnd(as);
		}
		return ups;
	}

	public AlgebraSymbolVector getSpouseArrowsx(){

		AlgebraSymbolVector ups = new AlgebraSymbolVector(1);
		AlgebraSymbolVector av = makeAlg.theAlgebra.getGenerators();

		for (av.reset();av.isNext();) {
			AlgebraSymbol as = av.getNext();
			if (Algebra.getCurrent().isArrow(as,Bops.SPOUSE)) ups.addToEnd(as);
		}
		return ups;
	}

	public boolean isArrowx(AlgebraSymbol as, int arrow){
		AlgebraSymbolVector fe = makeAlg.theAlgebra.getFocalElements();
		AlgebraSymbol rcp = as.getReciprocal();

		for(fe.reset();fe.isNext();) {
			AlgebraSymbol aft = fe.getNext();
			switch (arrow)	{
				case Bops.UP:
				    if (makeAlg.theAlgebra.isEquation(new Equation(rcp,aft).addLhs(as))) {
				    if (!makeAlg.theAlgebra.isEquation(new Equation(as,aft).addLhs(rcp))) {
						return true;
					}
				}
				return false;
				case Bops.DOWN:
				    if (makeAlg.theAlgebra.isEquation(new Equation(as,aft).addLhs(rcp))) {
				    if (!makeAlg.theAlgebra.isEquation(new Equation(rcp,aft).addLhs(as))) {
						return true;
					}
				    }
				    return false;
				case Bops.LEFT:
				    if ((makeAlg.theAlgebra.isEquation(new Equation(as,as).addLhs(as))) &&
				    !(Algebra.getCurrent().isArrow(as,Bops.UP)) && !(Algebra.getCurrent().isArrow(as,Bops.DOWN))) {
				    return true;
				    }
				    return false;
				case Bops.RIGHT:
				    if ((makeAlg.theAlgebra.isEquation(new Equation(as,as).addLhs(as))) &&
				    !(Algebra.getCurrent().isArrow(as,Bops.UP)) && !(Algebra.getCurrent().isArrow(as,Bops.DOWN))) {
				    return true;
				    }
				    return false;
				case Bops.SPOUSE:
					if (as.getSex().equals("N")) {
						if (makeAlg.theAlgebra.isEquation(new Equation(as,aft).addLhs(as))) {
						    return true;
						}
					}
					else if ((makeAlg.theAlgebra.isEquation(new Equation(as,makeAlg.theAlgebra.getCurrent().getElement("0")).addLhs(as))) &&
				    !(Algebra.getCurrent().isArrow(as,Bops.UP)) && !(Algebra.getCurrent().isArrow(as,Bops.DOWN))) {
						return true;
					}
					return false;
				}
		}
	return false;
    }*/
