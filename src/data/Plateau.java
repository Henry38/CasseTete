package data;

import java.awt.Point;
import java.util.ArrayList;

public class Plateau {
	
	private int width, height;
	private int[][] array;
	private ArrayList<Shape> arrayShape;
	
	/** Constructeur */
	public Plateau(int width, int height) {
		this.width = width+8;
		this.height = height+8;
		
		array = new int[getHeight()][];
		for (int i = 0; i < getHeight(); i++) {
			array[i] = new int[getWidth()];
			for (int j = 0; j < getWidth(); j++) {
				setBlock(i, j, 0);
			}
		}
		this.arrayShape = new ArrayList<Shape>();
	}
	
	/** Retourne la largeur du plateau */
	public int getWidth() {
		return width;
	}
	
	/** Retourne la hauteur du plateau */
	public int getHeight() {
		return height;
	}
	
	/** Retourne si la case (i, j) est a l'interieur du plateau */
	public boolean valid(int i, int j) {
		return (i >= 0 && j >= 0 && i < getHeight() && j < getWidth());
	}
	
	/** Retourne vrai si la case (i, j) n'est pas vide */
	public boolean busyCase(int i, int j) {
		return getBlock(i, j) != 0;
	}
	
	/** Retourne le contenu de la case (i, j) */
	public int getBlock(int i, int j) {
		return array[i][j];
	}
	
	/** Retourne la liste des shapes du plateau */
	public final ArrayList<Shape> getListShape() {
		return arrayShape;
	}
	
	/** Set le contenu de la case (i, j) */
	public void setBlock(int i, int j, int k) {
		array[i][j] = k;
	}
	
	
	
	/** Ajoute une shape au plateau */
	public void addShapeList(Shape shape) {
		arrayShape.add(shape);
		placeShape(shape);
	}
	
	/** Retire toutes les shapes du plateau */
	public void removeShapeList() {
		for (Shape shape : getListShape()) {
			removeShape(shape);
		}
		arrayShape.clear();
	}
	
	public void placeShape(Shape shape) {
		int line = shape.getLine();
		int column = shape.getColumn();
		for (int i = 0; i < shape.getHeight(); i++) {
			for (int j = 0; j < shape.getWidth(); j++) {
				if (shape.busyCase(i, j) && valid(line+i, column+j)) {
					array[line+i][column+j] += 1;
				}
			}
		}
	}
	
	public void removeShape(Shape shape) {
		int line = shape.getLine();
		int column = shape.getColumn();
		for (int i = 0; i < shape.getHeight(); i++) {
			for (int j = 0; j < shape.getWidth(); j++) {
				if (shape.busyCase(i, j) && valid(line+i, column+j)) {
					array[line+i][column+j] -= 1;
				}
			}
		}
	}
	
	/** Retourne vrai si la shape peut etre placee sur sa position */
	public boolean isValidLocation(Shape shape) {
		int line = shape.getLine();
		int column = shape.getColumn();
		if (line < 0 || column < 0 || line+shape.getHeight() > getHeight() || column+shape.getWidth() > getWidth()) {
			return false;
		}
		for (int i = 0; i < shape.getHeight(); i++) {
			for (int j = 0; j < shape.getWidth(); j++) {
				if (shape.busyCase(i, j) && getBlock(line+i, column+j) > 0) {
					return false;
				}
			}
		}
		return true;
	}
	
	
	public boolean checkWin() {
		for (int i = 0; i < getHeight(); i++) {
			for (int j = 0; j < getWidth(); j++) {
				if (i >= 4 && j >= 4 && j < getWidth()-4 && i < getHeight()-4) {
					if (getBlock(i, j) != 1) {
						return false;
					}
				} else {
					if (getBlock(i, j) != 0) {
						return false;
					}
				}
			}
		}
		return true;
	}
	
	private final ArrayList<Point> getAllMove(Shape shape) {
		ArrayList<Point> arrayPoint = new ArrayList<Point>();
		Point point = shape.getPoint();
		
		for (int i = 4; i < getHeight()-3-shape.getHeight(); i++) {
			for (int j = 4; j < getWidth()-3-shape.getWidth(); j++) {
				shape.setLocation(i, j);
				//placeShape(shape);
				if (isValidLocation(shape)) {
					arrayPoint.add(new Point(i, j));
				}
				//removeShape(shape);
			}
		}
		
		shape.setLocation(point);
		return arrayPoint;
	}
	
	public void resolution() {
		ArrayList<Point> old_arrayPoint = new ArrayList<Point>();
		for (Shape shape : getListShape()) {
			old_arrayPoint.add((Point) shape.getPoint().clone());
			removeShape(shape);
		}
		
		ArrayList<Point> arrayPoint = resolutionAux(new ArrayList<Shape>());
		if (arrayPoint == null) {
			System.out.println("Aucune solution");
			arrayPoint = old_arrayPoint;
		}
		
		for (Shape shape : getListShape()) {
			shape.setLocation(arrayPoint.get(getListShape().indexOf(shape)));
			placeShape(shape);
		}
	}
	
	private ArrayList<Point> resolutionAux(ArrayList<Shape> arrayShapeAux) {
		if (checkWin()) {
			return new ArrayList<Point>();
		}
		
		ArrayList<Point> arrayPoint = null;
		for (Shape tmp : getListShape()) {
			
			if (!arrayShapeAux.contains(tmp)) {
				for (Point point : getAllMove(tmp)) {
					tmp.setLocation(point);
					
					placeShape(tmp);
					arrayShapeAux.add(tmp);
					arrayPoint = resolutionAux(arrayShapeAux);
					arrayShapeAux.remove(tmp);
					removeShape(tmp);
					
					if (arrayPoint != null) {
						arrayPoint.add(0, tmp.getPoint());
						return arrayPoint;
					}
				}
			}
			
		}
		
		return null;
	}
	
	public void imprime() {
		String s = "";
		for (int i = 0; i < getHeight(); i++) {
			for (int j = 0; j < getWidth(); j++) {
				s += getBlock(i, j) + " ";
			}
			s += "\n";
		}
		System.out.println(s);
	}
	
}