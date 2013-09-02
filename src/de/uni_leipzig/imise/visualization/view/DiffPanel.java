package de.uni_leipzig.imise.visualization.view;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

import de.uni_leipzig.imise.data.CRFVersion;
import de.uni_leipzig.imise.data.DiffVersion;
import de.uni_leipzig.imise.data.Item;
import de.uni_leipzig.imise.data.VersionPair;
import de.uni_leipzig.imise.data.managment.DiffVersionManager;
import de.uni_leipzig.imise.data.managment.VersionManager;
import de.uni_leipzig.imise.visualization.controller.DiffTreeController;
import de.uni_leipzig.imise.visualization.controller.DiffVersionController;

public final class DiffPanel extends JPanel implements PropertyChangeListener{
	
	public static final String DIFF_CALC = "diffCalcAction"; 
	private VersionManager vm ;
	private DiffVersionManager dvm;
	private DiffVersionController dcc;
	private DiffTreeController dtc;
	private VersionTree diffTree;
	private JPanel diffTreePanel;
	private JScrollPane view;
	public DiffPanel(DiffTreeController dtc) {
		super();
		this.vm = VersionManager.getInstance();
		vm.addPropertyChangeListener(this);
		this.dvm = DiffVersionManager.getInstance();
		this.dcc = new DiffVersionController(vm,dvm,this);
		this.dtc = dtc;
		this.initGui();
	}
	
	public void initGui(){
		
		this.setLayout(new BorderLayout(0, 0));
		JPanel calcPanel = new JPanel();
		calcPanel.setLayout(new FlowLayout(FlowLayout.LEFT,2,2));
		
		JButton btnDiffCalc = new JButton("Unterschiede anzeigen");
		btnDiffCalc.setActionCommand(DIFF_CALC);
		btnDiffCalc.addActionListener(dcc);
		calcPanel.add(btnDiffCalc);
		this.add(calcPanel, BorderLayout.NORTH);
		
		diffTreePanel = new JPanel(new BorderLayout());
		TitledBorder tb =new TitledBorder("Diff-Baum");
		diffTreePanel.setBorder(tb);
		diffTreePanel.setPreferredSize(new Dimension(600,400));
		diffTree = new VersionTree(dtc);
		
		view = new JScrollPane();
		diffTreePanel.add(view,BorderLayout.CENTER);
		view.setViewportView(diffTree);
		add(diffTreePanel, BorderLayout.CENTER);
		
		
	}
	
	
	
	public void updateDiffTree() {
		Integer beforeKey = vm.getVersions().lastKey();
		CRFVersion lastVersion=vm.getVersions().get(beforeKey);
		diffTree.loadVersion(lastVersion);
		HashMap<String,List<String>> changeGraph = dvm.getChangeGraph();
		HashMap<Item,List<VersionPair>> itemChangedPerVersion = dvm.getDiffVersionsPerItem(lastVersion);
		for (Entry<Item,List<VersionPair>> e: itemChangedPerVersion.entrySet()){
			List<VersionPair> diffVersions = e.getValue();
			Item i = e.getKey();
			List<String> items = changeGraph.get(i.getItemLabel());
			for (int ver = 0;ver<diffVersions.size();ver++){
				VersionPair vp = diffVersions.get(ver);
				String itemLabel = items.get(ver);
				DiffVersion dv = dvm.getDiffVersionMap().get(vp);
				
				if(itemLabel.equals(i.getItemLabel())){
					diffTree.addDiffForItem(itemLabel, CellConstants.ADD_TYPE, vp.getV1(), vp.getV2(), itemLabel);
				}else{
					CRFVersion v = vm.getVersions().get(vp.getV1());
					Item oldItem = v.getItems().get(itemLabel);
					if (dv.getOldNewItemMap().containsKey(oldItem)){
						diffTree.addDiffForItem(e.getKey().getItemLabel(), CellConstants.MOD_TYPE, vp.getV1(), vp.getV2(), itemLabel);
					}
						
				}
			}
		}
		diffTree.updateUI();
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
				this.dvm.clearAll();
			}
		}
		
	}

	
}