package de.uni_leipzig.imise.visualization.view;
import javax.swing.JFrame;
import javax.swing.JPanel;

import de.uni_leipzig.imise.data.CRFVersion;
import de.uni_leipzig.imise.data.managment.VersionManager;
import de.uni_leipzig.imise.visualization.controller.VersionController;

import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.FlowLayout;
import javax.swing.JLabel;
import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.logging.Logger;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;

public final class VersionPanel extends JPanel implements PropertyChangeListener{
	
	
	private static final Logger log = Logger.getLogger(VersionPanel.class.getName());
	public static final String ADD_VERSION_TREE = "addVersionTree";
	private static final long serialVersionUID = 1L;

	private VersionController vmc;
	
	private VersionManager vm ;
	
	private JPanel versionTreePanel;
	private JComboBox<Integer> comboBox;
	
	private HashMap<Integer,JInternalFrame> versionFrameMap;

	private DefaultComboBoxModel<Integer> comboVersionM;

	private JLabel crfNamelabel;
	
	public VersionPanel (){
		super();
		new PropertyChangeSupport(this);
		this.versionFrameMap = new HashMap<Integer,JInternalFrame>();
		vm =VersionManager.getInstance();
		vm.addPropertyChangeListener(this);
		vmc = new VersionController(vm,this);
		this.initGui();
	}
	
	private void initGui() {
		setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		add(panel, BorderLayout.NORTH);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		crfNamelabel = new JLabel("");
		panel.add(crfNamelabel);
		
		JLabel lblVersions = new JLabel("version choice");
		lblVersions.setForeground(Color.BLUE);
		panel.add(lblVersions);
		
		comboBox = new JComboBox<Integer>();
		comboVersionM = new DefaultComboBoxModel<Integer>();
		comboBox.setModel(comboVersionM);
		panel.add(comboBox);
		
		JButton btnAddVersion = new JButton("show version");
		btnAddVersion.setActionCommand(ADD_VERSION_TREE);
		btnAddVersion.addActionListener(vmc);
		panel.add(btnAddVersion);
		
		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane);
		versionTreePanel = new JPanel();
		versionTreePanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		scrollPane.setViewportView(versionTreePanel);
		versionTreePanel.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
		
	}
	
	public void addVersionTree(int version){
		CRFVersion v = vm.getVersions().get(version);
		TreeFrame tf = (TreeFrame) this.versionFrameMap.get(version);
		if (tf==null){
			tf = new TreeFrame (version,vmc);
			tf.initTree(v);
			tf.pack();
			this.versionFrameMap.put(version, tf);
		}	
		this.versionTreePanel.add(tf);
		
	}
	
	
	public void removeTreeFrame(int version){
		this.versionFrameMap.remove(version);
	}
	
	public Integer getSelectedVersion (){
		return (Integer) comboVersionM.getSelectedItem();
	}
	
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		String name = evt.getPropertyName();
		Object obj = evt.getSource();
		
		if (obj instanceof VersionManager){
			if (name.equals(VersionManager.ADD_VERSIONS)){
				comboVersionM.removeAllElements();
				TreeMap<Integer, CRFVersion> versions = (TreeMap<Integer, CRFVersion>) evt.getNewValue();
				crfNamelabel.setText(versions.firstEntry().getValue().getName());
				for (Entry<Integer,CRFVersion> v:versions.entrySet())
					comboVersionM.addElement(v.getValue().getVersion());
			}else if (name.equals(VersionManager.CLEAR_VERSIONS)){
				log.info("release version frames");
				this.comboVersionM.removeAllElements();
				for (Integer k: this.versionFrameMap.keySet()){
					TreeFrame tf = (TreeFrame) this.versionFrameMap.get(k);
				
					//this.versionFrameMap.put(k, null);
					tf.release();
					//tf.dispose();
				}
				this.versionTreePanel.removeAll();
				this.versionFrameMap.clear();
				this.versionTreePanel.updateUI();
			}
		}
		
	}

	public VersionController getVersionController(){
		return this.vmc;
	}
	
	public static void main (String[] args){
		JFrame f = new JFrame ();
		f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		f.setContentPane(new VersionPanel());
	
	}

}