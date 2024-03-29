/*
	A basic extension of the java.awt.Dialog class
 */

import java.awt.*;
import java.awt.event.*;

public class ElementPanel extends Panel
{
	Kaes parent = null;
	
	public void setParent(Kaes parent) {
		this.parent = parent;
	}
	
	public ElementPanel()
	{
		// This code is automatically generated by Visual Cafe when you add
		// components to the visual environment. It instantiates and initializes
		// the components. To modify the code, only use code syntax that matches
		// what Visual Cafe can generate, or Visual Cafe may be unable to back
		// parse your Java file into its visual environment.
		//{{INIT_CONTROLS
		setLayout(null);
		setSize(283,155);
		spouseButton.setCheckboxGroup(Group1);
		spouseButton.setLabel("Spouse");
		add(spouseButton);
		spouseButton.setBounds(183,117,99,28);
		generatorButton.setCheckboxGroup(Group1);
		generatorButton.setState(true);
		generatorButton.setLabel("Generator");
		add(generatorButton);
		generatorButton.setBounds(183,86,99,28);
		identityButton.setCheckboxGroup(Group1);
		identityButton.setLabel("Identity");
		add(identityButton);
		identityButton.setBounds(183,55,99,28);
		itemLabel.setText("Generator");
		add(itemLabel);
		itemLabel.setBounds(8,57,74,31);
		add(itemField);
		itemField.setFont(new Font("Serif", Font.PLAIN, 14));
		itemField.setBounds(88,55,43,35);
		messageArea.setEditable(false);
		add(messageArea);
		messageArea.setBounds(7,4,272,49);
		cancelButton.setLabel("Cancel");
		add(cancelButton);
		cancelButton.setBounds(5,125,60,23);
		addButton.setLabel("Add Generator");
		add(addButton);
		addButton.setBounds(32,92,99,24);
		helpButton.setLabel("Help");
		add(helpButton);
		helpButton.setBounds(110,126,60,23);
		//}}

		//{{REGISTER_LISTENERS
	//	SymWindow aSymWindow = new SymWindow();
		//this.addWindowListener(aSymWindow);
		SymAction lSymAction = new SymAction();
		helpButton.addActionListener(lSymAction);
		cancelButton.addActionListener(lSymAction);
		addButton.addActionListener(lSymAction);
		SymItem lSymItem = new SymItem();
		identityButton.addItemListener(lSymItem);
		generatorButton.addItemListener(lSymItem);
		spouseButton.addItemListener(lSymItem);
		//}}
	}
	
	public void addNotify()
	{
  	    // Record the size of the window prior to calling parents addNotify.
	    Dimension d = getSize();

		super.addNotify();

		if (fComponentsAdjusted)
			return;

		// Adjust components according to the insets
		setSize(insets().left + insets().right + d.width, insets().top + insets().bottom + d.height);
		Component components[] = getComponents();
		for (int i = 0; i < components.length; i++)
		{
			Point p = components[i].getLocation();
			p.translate(insets().left, insets().top);
			components[i].setLocation(p);
		}
		fComponentsAdjusted = true;
	}

    // Used for addNotify check.
	boolean fComponentsAdjusted = false;



    /**
     * Shows or hides the component depending on the boolean flag b.
     * @param b  if true, show the component; otherwise, hide the component.
     * @see java.awt.Component#isVisible
     */
/*    public void setVisible(boolean b)
	{
		if(b)
		{
			Rectangle bounds = getParent().getBounds();
			Rectangle abounds = getBounds();
	
			setLocation(bounds.x + (bounds.width - abounds.width)/ 2,
				 bounds.y + (bounds.height - abounds.height)/2);
		}
		super.setVisible(b);
	}
*/
	//{{DECLARE_CONTROLS
	java.awt.Checkbox spouseButton = new java.awt.Checkbox();
	java.awt.CheckboxGroup Group1 = new java.awt.CheckboxGroup();
	java.awt.Checkbox generatorButton = new java.awt.Checkbox();
	java.awt.Checkbox identityButton = new java.awt.Checkbox();
	java.awt.Label itemLabel = new java.awt.Label();
	java.awt.TextField itemField = new java.awt.TextField();
	java.awt.TextArea messageArea = new java.awt.TextArea("",0,0,TextArea.SCROLLBARS_NONE);
	java.awt.Button cancelButton = new java.awt.Button();
	java.awt.Button addButton = new java.awt.Button();
	java.awt.Button helpButton = new java.awt.Button();
	//}}
/*
	class SymWindow extends java.awt.event.WindowAdapter
	{
		public void windowClosing(java.awt.event.WindowEvent event)
		{
			Object object = event.getSource();
			if (object == ElementDialog.this)
				ElementDialog_WindowClosing(event);
		}
	}
	
	void ElementDialog_WindowClosing(java.awt.event.WindowEvent event)
	{
		setVisible(false);
	}
*/
	class SymAction implements java.awt.event.ActionListener
	{
		public void actionPerformed(java.awt.event.ActionEvent event)
		{
			Object object = event.getSource();
			if (object == helpButton)
				helpButton_ActionPerformed(event);
			else if (object == cancelButton)
				cancelButton_ActionPerformed(event);
			else if (object == addButton)
				addButton_ActionPerformed(event);
		}
	}

	void helpButton_ActionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		//{{CONNECTION
		// Enable the ElementDialog
		setEnabled(true);
		//}}
	}

	void cancelButton_ActionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		//{{CONNECTION
		// Show the ElementDialog
		setVisible(false);
		theElement=oldElement;
		setElementType(oldElementType);
		ActionEvent a = new ActionEvent(this,ActionEvent.ACTION_PERFORMED,"Cancel");
		parent.elementDialog_ActionPerformed(a);
		//}}
	}

	void addButton_ActionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		//{{CONNECTION
		// Toggle show/hide
		setVisible(false);
		ActionEvent a = new ActionEvent(this,ActionEvent.ACTION_PERFORMED,"OK");
		parent.elementDialog_ActionPerformed(a);
		//}}
	}

	class SymItem implements java.awt.event.ItemListener
	{
		public void itemStateChanged(java.awt.event.ItemEvent event)
		{
			Object object = event.getSource();
			if (object == identityButton)
				identityButton_ItemStateChanged(event);
			else if (object == generatorButton)
				generatorButton_ItemStateChanged(event);
			else if (object == spouseButton)
				spouseButton_ItemStateChanged(event);
		}
	}

	void identityButton_ItemStateChanged(java.awt.event.ItemEvent event)
	{
		// to do: code goes here.
			 
		//{{CONNECTION
		// Set the text for Label Get the RadioButton's label
		itemLabel.setText(identityButton.getLabel());
		elementType = IDENTITY;
		identityButton.setState(true);
		//}}
	}

	void generatorButton_ItemStateChanged(java.awt.event.ItemEvent event)
	{
		// to do: code goes here.
			 
		//{{CONNECTION
		// Set the text for Label Get the RadioButton's label
		itemLabel.setText(generatorButton.getLabel());
		elementType = GENERATOR;
		generatorButton.setState(true);
		//}}
	}

	void spouseButton_ItemStateChanged(java.awt.event.ItemEvent event)
	{
		// to do: code goes here.
			 
		//{{CONNECTION
		// Set the text for Label Get the RadioButton's label
		itemLabel.setText(spouseButton.getLabel());
		elementType = SPOUSE;
		spouseButton.setState(true);

		//}}
	}
	
	public int getElementType() {
		return elementType;
	}
	
	public void setElementType(int t) {
		switch (t) {
			case IDENTITY: identityButton_ItemStateChanged(null);
							break;
			case GENERATOR: generatorButton_ItemStateChanged(null);
							break;
			case SPOUSE: spouseButton_ItemStateChanged(null);
							break;
		}
	}
	
	public String getElement() {
		return theElement;
	}
	
	public void setMessage(String s) {
		messageArea.setText(s);
	}
	
	public void doDialog() {
		oldElement=theElement;
		oldElementType=elementType;
		theElement = "";
		itemField.setText("");
		setElementType(elementType);
		this.show();
	}
	
	public final static int IDENTITY = 1;
	public final static int GENERATOR = 2;
	public final static int SPOUSE = 3;
	
	int elementType = GENERATOR;
	int oldElementType = 0;
	
	String theElement="";
	String oldElement="";
	
	

	/** 
     * Paints the container.  This forwards the paint to any lightweight components 
     * that are children of this container.  If this method is reimplemented, 
     * super.paint(g) should be called so that lightweight components are properly
     * rendered.  If a child component is entirely clipped by the current clipping
     * setting in g, paint() will not be forwarded to that child.
     *
     * @param g the specified Graphics window
     * @see   java.awt.Component#update(java.awt.Graphics)
     */
    public void paint(Graphics g) {
		super.paint(g);
		Rectangle r = getBounds();
		g.drawRect(2,2,r.width-4, r.height-4);
		// to do: place event handler code here.
	}
}
