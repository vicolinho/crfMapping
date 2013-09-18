package de.uni_leipzig.imise.visualization.view;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;

import de.uni_leipzig.imise.data.CRFVersion;
import de.uni_leipzig.imise.data.DiffVersion;
import de.uni_leipzig.imise.data.Item;
import de.uni_leipzig.imise.data.VersionPair;
import de.uni_leipzig.imise.data.constants.CategoryConstants;
import de.uni_leipzig.imise.data.managment.DiffVersionManager;
import de.uni_leipzig.imise.data.managment.VersionManager;
import de.uni_leipzig.imise.visualization.controller.DiffTreeController;
import de.uni_leipzig.imise.visualization.controller.DiffVersionController;

public final class DiffPanel extends JPanel implements PropertyChangeListener{
	
	private static final Logger log = Logger.getLogger(DiffPanel.class.getName());
	private VersionManager vm ;
	private DiffVersionManager dvm;
	private DiffVersionController dvc;
	private DiffTreeController dtc;
	private VersionTree diffTree;
	private JPanel diffTreePanel;
	private JScrollPane view;
	private JSplitPane splitPane;
	private JPanel deletedPanel;
	private CRFTableModel deletedItemModel;
	private JTable deletedTable;
	private JMenu mnNewMenu;
	private List <JCheckBoxMenuItem> catItems;

	private JMenuBar menuBar;
	public DiffPanel(DiffTreeController dtc) {
		super();
		this.vm = VersionManager.getInstance();
		vm.addPropertyChangeListener(this);
		this.dvm = DiffVersionManager.getInstance();
		this.dvc = new DiffVersionController(vm,dvm,this);
		this.dtc = dtc;
		this.catItems =new ArrayList<JCheckBoxMenuItem>();
		this.initGui();
	}
	
	public void initGui(){
		
		this.setLayout(new BorderLayout(0, 0));
		JPanel calcPanel = new JPanel();
		calcPanel.setLayout(new FlowLayout(FlowLayout.LEFT,2,2));
		
		JButton btnDiffCalc = new JButton("show changes");
		btnDiffCalc.setActionCommand(EventConstants.DIFF_CALC);
		btnDiffCalc.addActionListener(dvc);
		calcPanel.add(btnDiffCalc);
		this.add(calcPanel, BorderLayout.NORTH);
		
		menuBar = new JMenuBar();
		calcPanel.add(menuBar);
		
		mnNewMenu = new JMenu("category selection");
		menuBar.add(mnNewMenu);
		this.initCategoryItems();
		
		TitledBorder tb =new TitledBorder("diff-tree");
		
		splitPane = new JSplitPane();
		splitPane.setResizeWeight(0.8);
		
		diffTreePanel = new JPanel(new BorderLayout());
		diffTreePanel.setBorder(tb);
		diffTreePanel.setPreferredSize(new Dimension(600,400));
		
		diffTree = new VersionTree(dtc);
		diffTree.getSelectionModel().addTreeSelectionListener(dtc);
		view = new JScrollPane();
		diffTreePanel.add(view,BorderLayout.CENTER);
		view.setViewportView(diffTree);
		splitPane.setLeftComponent(diffTreePanel);
		TitledBorder tb2 = new TitledBorder("deleted items");
		deletedPanel = new JPanel (new BorderLayout());
		deletedPanel.setBorder(tb2);
		deletedItemModel =new CRFTableModel(CellConstants.VERSION_COL,CellConstants.ITEM_COL);
		deletedTable = new JTable(deletedItemModel);
		deletedTable.getSelectionModel().addListSelectionListener(dvc);
		deletedTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane pane =new JScrollPane(deletedTable);	
		deletedPanel.add(pane,BorderLayout.CENTER);
		splitPane.setRightComponent(deletedPanel);
		add(splitPane, BorderLayout.CENTER);
		
		
	}
	
	
	
	private void initCategoryItems() {
		for (String cat : CategoryConstants.DEFAULT_CATS){
			JCheckBoxMenuItem cbi = new JCheckBoxMenuItem(cat);
			cbi.setActionCommand(cat);
			cbi.setSelected(true);
			cbi.addActionListener(dvc);
			this.mnNewMenu.add(cbi);
			this.catItems.add(cbi);
			
			
		}
		
	}
	
	public void  updateColorTree(){
		List<String> selCats = new ArrayList <String>();
		if (!dvm.isEmpty()){
			for (JCheckBoxMenuItem bi :this.catItems){
				if (bi.isSelected()){
					selCats.add(bi.getActionCommand());
				}
			}
			this.diffTree.updateCategoryColors(
					dvm.getCategoryItemMap(), selCats.toArray(new String[]{}));
			this.diffTree.updateUI();
		}
		
	}

	public void updateDiffTree() {
		Integer beforeKey = vm.getVersions().lastKey();
		CRFVersion lastVersion=vm.getVersions().get(beforeKey);
		diffTree.loadVersion(lastVersion);
		dtc.setVersion(beforeKey);
		HashMap<String,List<String>> changeGraph = dvm.getChangeGraph();
		HashMap<Item,List<VersionPair>> itemChangedPerVersion = dvm.getDiffVersionsPerItem(lastVersion);
		diffTree.updateCategoryColors(dvm.getCategoryItemMap(),
				CategoryConstants.DEFAULT_CATS);
		
		for (Entry<Item,List<VersionPair>> e: itemChangedPerVersion.entrySet()){
			List<VersionPair> diffVersions = e.getValue();
			Item i = e.getKey();
			List<String> items = changeGraph.get(i.getItemLabel());
			for (int ver = 0;ver<diffVersions.size();ver++){
				VersionPair vp = diffVersions.get(ver);
				String itemLabel = items.get(ver);
				DiffVersion dv = dvm.getDiffVersionMap().get(vp);
				CRFVersion v = vm.getVersions().get(vp.getV1());
				Item oldItem = v.getItems().get(itemLabel);
				if(!dv.getOldNewItemMap().containsKey(oldItem)){
					diffTree.addDiffForItem(itemLabel, CellConstants.ADD_TYPE, vp.getV1(), vp.getV2(), itemLabel);
				}else{	
					diffTree.addDiffForItem(e.getKey().getItemLabel(), CellConstants.MOD_TYPE, vp.getV1(), vp.getV2(), itemLabel);
				}
			}
		}
		diffTree.updateUI();
	}
	
	public void updateDeletedTable(){
		deletedItemModel.clear();
		VersionPair beforeKey = dvm.getDiffVersionMap().lastKey();
		
		do{
			DiffVersion dv = dvm.getDiffVersionMap().get(beforeKey);
			List<Item> delItems = dv.getDeletedItems();
			for (Item di : delItems){
				int r = this.deletedItemModel.addRow();
				this.deletedItemModel.setValueAt(beforeKey.getV1(), r, CellConstants.VERSION_COL);
				this.deletedItemModel.setValueAt(di.getItemLabel(), r, CellConstants.ITEM_COL);
			}
			
			beforeKey = dvm.getDiffVersionMap().lowerKey(beforeKey);
		}while (beforeKey !=null);
	}
	
	public void clearAllComponents(){
		this.diffTree.clearSelection();
		this.diffTree.release();
		deletedItemModel.clear();
		for (JCheckBoxMenuItem bi : catItems){
			bi.setSelected(true);
		}
		
		
	}
	
	public static void  main(String[] arg){
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		f.setContentPane(new DiffPanel(null));
		f.pack();
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if(evt.getSource() instanceof VersionManager){
			if (evt.getPropertyName().equals(VersionManager.CLEAR_VERSIONS)){
				log.info("release Diff Panel components");
				this.dvm.clearAll();
				this.clearAllComponents();
			}
		}
		
	}

	public JTable getDeletedTable() {
		// TODO Auto-generated method stub
		return this.deletedTable;
	}

	public DiffVersionController getDiffController() {
		return this.dvc;
		
	}

	
}