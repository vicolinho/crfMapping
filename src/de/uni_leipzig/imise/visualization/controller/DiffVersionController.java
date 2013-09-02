package de.uni_leipzig.imise.visualization.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map.Entry;

import javax.swing.JOptionPane;

import de.uni_leipzig.imise.data.CRFVersion;
import de.uni_leipzig.imise.data.DiffVersion;
import de.uni_leipzig.imise.data.VersionPair;
import de.uni_leipzig.imise.data.managment.DiffVersionManager;
import de.uni_leipzig.imise.data.managment.VersionManager;
import de.uni_leipzig.imise.diff.calculation.DiffCalculator;
import de.uni_leipzig.imise.visualization.view.DiffPanel;

public class DiffVersionController implements ActionListener{

	
	private VersionManager vm;
	private DiffVersionManager dvm;
	private DiffPanel dvp;
	private DiffCalculator diffCalculator;

	public DiffVersionController(VersionManager vm, DiffVersionManager dvm,
			DiffPanel dvp){
		this.vm = vm;
		this.dvm = dvm;
		this.dvp = dvp;
		this.diffCalculator = new DiffCalculator();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(DiffPanel.DIFF_CALC)){
			if (!vm.getVersions().isEmpty()){
				for (Entry<Integer,CRFVersion> entry:vm.getVersions().entrySet()){
					Integer nextKey = vm.getVersions().higherKey(entry.getKey());
					if (nextKey!=null){
						CRFVersion nextVersion = vm.getVersions().get(nextKey);
						//diffCalculator.clearAll();
						diffCalculator.calculateDiff(entry.getValue(), nextVersion);
						VersionPair vp = new VersionPair(entry.getKey(),nextKey);
						DiffVersion dv = new DiffVersion();
						dv.setAddedItems(diffCalculator.getAddedItems());
						dv.setDeletedItems(diffCalculator.getDeletedItems());
						dv.setEqualItems(diffCalculator.getEqualItems());
						dv.setModifiedItems(diffCalculator.getModifiedItems());
						dv.setNewOldItemMap(diffCalculator.getNewOldItemMap());
						dv.setOldNewItemMap(diffCalculator.getOldNewItemMap());
						dvm.addDiff(vp, dv);
						
						
					}
				}
				dvp.updateDiffTree();
				dvp.updateDeletedTable();
			}else {
				JOptionPane.showConfirmDialog(null, "Sie müssen eine Menge von CRF Versionen vorab laden", "Keine Versionen", JOptionPane.WARNING_MESSAGE);
			}
		}
		
	}
}
