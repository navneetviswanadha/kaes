import java.awt.*;
import java.util.*;

public class Table extends Panel {
	int [] colwidths=null;
	String [] colnames=null;
	Object [][] cellData=null;
	int rowCount=0;
	int colCount=0;
	Font plainFont = new Font("SansSerif", Font.PLAIN, 9);
	Font boldFont = new Font("SansSerif", Font.BOLD, 9);
	Font bigFont = new Font("SansSerif", Font.BOLD, 12);
	String title="None";
	
	Table(String title, Object [][] data, String[] columnNames) {
		this.title = title;
		colwidths = new int[columnNames.length];
		colnames = columnNames;
		cellData = data;
		colCount = columnNames.length;
		rowCount = data.length;
		setFont(plainFont);
		setSize(580,580);
	}

	Table(String title, Vector data, Vector columnNames) {
		this.title = title;
		setData(data,columnNames);
	}

	public Dimension getPreferredSize() {
	   Dimension d = getSize();
	   return d;
	}
	
	public void setData(Vector data, Vector columnNames) {
		if (data == null || columnNames == null || data.size() == 0 || columnNames.size() == 0) {
			data = new Vector();
			Vector q = new Vector();
			q.addElement("no data");
			data.addElement(q);
			columnNames = new StringVector();
			columnNames.addElement("No Data");
		}
		colwidths = new int[columnNames.size()];
		for (int i=0;i<colwidths.length;i++) colwidths[i] = 0;
		colnames = new String[columnNames.size()];
		for (int i=0;i<colnames.length;i++) colnames[i] =  (String) columnNames.elementAt(i);
		for (int i=0;i<colnames.length;i++) colwidths[i] = colnames[i].length()+2;
		cellData = new Object[data.size()][columnNames.size()];
		for (int i=0;i<data.size();i++) {
			Vector k = (Vector) data.elementAt(i);
			for (int j=0;j<colnames.length;j++) {
			   if (k.elementAt(j) == null) cellData[i][j] = "Null";
			   else cellData[i][j] = k.elementAt(j);
				int w = cellData[i][j].toString().length()+2;
				if (w > colwidths[j]) colwidths[j] = w;
			}
		}
		colCount = columnNames.size();
		rowCount = data.size();
		setFont(plainFont);
	}
	
	public void paint(Graphics g) {
		int y = 20;
		int lastx = 0;
		int w = 0;
		g.setFont(bigFont);
		g.drawString(title,colwidths[0],y);
		g.setFont(boldFont);
		y+=20;
		for (int i=0;i<colCount;i++){
			lastx = lastx + 5 +w;
			w = colwidths[i] * (i == 0 ? 6 : 5);
			g.drawString(colnames[i],lastx,y);
		//	Debug.prout(4,i+" "+colnames[i]+"   ");
		}
		g.drawRect(1,3,lastx+w,y+5);
	//	g.drawLine(1,3,1,y+5);
	//	g.drawLine(1,y+5,lastx+w,y+5);
	//	g.drawLine(lastx+w,3,lastx+w,y+5);
		y+=20;
		for(int i=0;i<rowCount;i++) {
		//	Debug.prout(4,"");
			lastx = 0;
			y += 24;
			w = 3;
			g.setFont(boldFont);
			Color c = g.getColor();
			for(int j=0;j<colCount;j++) {
				String contents = (String) cellData[i][j].toString();
				if (cellData[i][j] instanceof ColorTerm)
					g.setColor(((ColorTerm) cellData[i][j]).getTheColor());
				else g.setColor(c);
				lastx = lastx + 5+ w;
				w = colwidths[j]*(j == 0 ? 6 : 5);
				g.drawString(contents,lastx,y);
				g.setFont(plainFont);
//				Debug.prout(4,i+" "+contents+"   ");
			}
			g.setColor(Color.lightGray);
			g.drawRect(1,y-16,lastx+w,24);
			g.setColor(Color.gray);
			g.drawRect(2,y-17,lastx+w,24);
			g.setColor(Color.black);
			g.drawRect(3,y-18,lastx+w,24);
		}
		setSize(lastx+w,y);
	}
	
	
	int getRowCount() {
		return rowCount;
	}
	
	int getColumnCount() {
		return colCount;
	}
}
