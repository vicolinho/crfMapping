package de.uni_leipzig.imise.visualization.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EventObject;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.event.CellEditorListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreeCellRenderer;

import de.uni_leipzig.imise.data.CRFVersion;
import de.uni_leipzig.imise.data.Item;
import de.uni_leipzig.imise.visualization.controller.DiffTreeController;

public class VersionTree extends JTree{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(VersionTree.class.getName());
	private DefaultTreeModel model;
	
	private DefaultMutableTreeNode root;
	private DiffTreeController treeCtrl;
	private int version;
	private HashMap<String,DefaultMutableTreeNode> itemNodeMap;
	private TableNodeRenderer nodeRenderer;
	private EditorNodeRenderer nodeEditor;
	public VersionTree (DiffTreeController tCtrl) {
		super();
		this.treeCtrl = tCtrl;
		nodeRenderer = new TableNodeRenderer();
		nodeEditor = new EditorNodeRenderer(treeCtrl);
		this.setCellEditor(nodeEditor);
		this.setCellRenderer(nodeRenderer);
		this.setEditable(true);
		this.setRowHeight(0);

		this.root = new DefaultMutableTreeNode("version");
		this.model = new DefaultTreeModel(root);
		this.itemNodeMap = new HashMap<String,DefaultMutableTreeNode>();
		
		this.setModel(model);
		
		this.getSelectionModel().addTreeSelectionListener(treeCtrl);
		
	}
	
	public VersionTree () {
		super();
		this.setRowHeight(0);
		this.root = new DefaultMutableTreeNode("version");
		this.model = new DefaultTreeModel(root);
		this.itemNodeMap = new HashMap<String,DefaultMutableTreeNode>();
		this.setModel(model);
		
	}
	
	
	public void loadVersion(CRFVersion v){
		this.version = v.getVersion();
		this.root.setUserObject(v.getName()+v.getVersion());
		List <Item> itemList = new ArrayList<Item>();
		itemList.addAll(v.getItems().values());
		Collections.sort(itemList);
		for (Item i:itemList){
			DefaultMutableTreeNode in = new DefaultMutableTreeNode(i.getItemLabel());
			this.itemNodeMap.put(i.getItemLabel(), in);
			this.root.add(in);	
			
		}
		
	}
	
	@Override
	public Insets getInsets() {
		return new Insets(5,5,5,5);
	}
	
	
	public void addDiffForItem(String itemLabel, String type,int from ,int until,String modItem){
		DefaultMutableTreeNode in= this.itemNodeMap.get(itemLabel);
		DefaultMutableTreeNode diffNode;
		CRFTableModel tableModel;
		if (in.getChildCount()==0){
			diffNode = new DefaultMutableTreeNode();
			tableModel = new CRFTableModel(CellConstants.VERSION_COL,CellConstants.ITEM_COL,
					CellConstants.TYPE_COL);
			diffNode.setUserObject(tableModel);
			in.add(diffNode);
		}else {
			diffNode = (DefaultMutableTreeNode) in.getChildAt(0);
			tableModel = (CRFTableModel) diffNode.getUserObject();
		}
		
	
		if (type.equals(CellConstants.MOD_TYPE)){
			int r = tableModel.addRow();
			tableModel.setValueAt(new Integer(from), r, 0);
			tableModel.setValueAt(modItem, r, 1);
			tableModel.setValueAt(type, r, 2);
		}else if(type.equals(CellConstants.ADD_TYPE)){
			int r = tableModel.addRow();
			tableModel.setValueAt(new Integer(from), r, 0);
			tableModel.setValueAt(modItem, r, 1);
			tableModel.setValueAt(type, r, 2);
		}
		
	}
	
	public void release(boolean isVersionTree){
		
		this.root.setUserObject("version");
		this.root.removeAllChildren();
		if (!isVersionTree)
			this.getSelectionModel().removeTreeSelectionListener(treeCtrl);
		this.itemNodeMap.clear();
	}
	
	public void releaseVersionTree(){
		
	}
	
	
	
	private class TableNodeRenderer extends JPanel implements TreeCellRenderer{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		private DefaultTreeCellRenderer dr;
		private JTable dt;

		private JScrollPane view;
		TableNodeRenderer(){
			super(new BorderLayout());
			dr = new DefaultTreeCellRenderer();
			dt = new JTable();
			view = new JScrollPane();
			add(view,BorderLayout.CENTER);
			view.setViewportView(dt);
		}
		
		@Override
		public Component getTreeCellRendererComponent(JTree tree, Object value,
				boolean selected, boolean expanded, boolean leaf, int row,
				boolean hasFocus) {
			
			DefaultMutableTreeNode n = (DefaultMutableTreeNode) value;
			if (n.getUserObject() instanceof CRFTableModel){
				CRFTableModel dtm =(CRFTableModel) n.getUserObject();
				dt.setModel(dtm);
				dt.getColumn(CellConstants.ITEM_COL).setPreferredWidth(150);
				dt.getColumn(CellConstants.VERSION_COL).setPreferredWidth(75);
				dt.getColumn(CellConstants.TYPE_COL).setPreferredWidth(100);
				dt.setPreferredScrollableViewportSize(dt.getPreferredSize());
				
				
				//dt.setM
				return this;
			}else {
				return dr.getTreeCellRendererComponent(
						tree, value, selected, expanded, leaf, row, hasFocus);
			}
		}
		
	}
	
	private class EditorNodeRenderer extends JPanel implements TreeCellEditor{

		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private JTable dte;
		
		public EditorNodeRenderer(DiffTreeController treeCtrl) {
			super();
			dte = new JTable();
			dte.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			treeCtrl.setT(dte);
			dte.getSelectionModel().addListSelectionListener(treeCtrl);
			JScrollPane view2 = new JScrollPane(dte);
			add(view2);
		}
		@Override
		public Object getCellEditorValue() {
			return null;
		}

		@Override
		public boolean isCellEditable(EventObject anEvent) {
			
			JTree tree = (JTree) anEvent.getSource();
			MouseEvent me =(MouseEvent) anEvent;
			try{
				DefaultMutableTreeNode o = (DefaultMutableTreeNode) 
						tree.getPathForLocation(me.getX(), me.getY()).getLastPathComponent();
				if (o.getUserObject() instanceof CRFTableModel){
					return true;
				}else return false;
			}catch (NullPointerException e){
				return false;
			}
			
		}

		@Override
		public boolean shouldSelectCell(EventObject anEvent) {
			 return false;
		}

		@Override
		public boolean stopCellEditing() {
			
			return false;
		}

		@Override
		public void cancelCellEditing() {
		}

		@Override
		public void addCellEditorListener(CellEditorListener l) {	
		}

		@Override
		public void removeCellEditorListener(CellEditorListener l) {	
		}

		@Override
		public Component getTreeCellEditorComponent(JTree tree, Object value,
				boolean isSelected, boolean expanded, boolean leaf, int row) {
			DefaultMutableTreeNode n = (DefaultMutableTreeNode) value;
			if (n.getUserObject() instanceof CRFTableModel){
				CRFTableModel dtm =(CRFTableModel) n.getUserObject();
				dte.setModel(dtm);
				//dte.invalidate();
				dte.getColumn(CellConstants.ITEM_COL).setPreferredWidth(150);
				dte.getColumn(CellConstants.VERSION_COL).setPreferredWidth(75);
				dte.getColumn(CellConstants.TYPE_COL).setPreferredWidth(100);
				Dimension d = dte.getPreferredSize();
				d.height+=dte.getRowHeight()+7;
				dte.setPreferredScrollableViewportSize(d);
				
				return this;
			}else return null;
		}
		
	}
	
	public static void main (String[] args){
		JFrame f = new JFrame ();
		f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		f.setContentPane(new VersionTree(null));
	
	}

	/**
	 * @return the version
	 */
	public int getVersion() {
		return version;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(int version) {
		this.version = version;
	}

	
	
}
