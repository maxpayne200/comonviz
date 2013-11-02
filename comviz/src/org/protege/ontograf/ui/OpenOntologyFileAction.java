package org.protege.ontograf.ui;

import java.net.URI;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import ca.uvic.cs.chisel.cajun.actions.CajunAction;
import ca.uvic.cs.chisel.cajun.resources.ResourceHandler;
import comonviz.EntryPoint;

public class OpenOntologyFileAction extends CajunAction {
	private static final long serialVersionUID = 2406231898001180745L;

	private static final String ACTION_NAME = "Open an .owl file";

	private EntryPoint entryPoint;

	private URI uri;

	public OpenOntologyFileAction() {
		super(ACTION_NAME, new ImageIcon(OpenOntologyFileAction.class.getResource("/open.gif")));
	}

	@Override
	public void doAction() {

		JFileChooser chooser = new JFileChooser();
		chooser.setMultiSelectionEnabled(false);
		FileFilter fileFilter = new FileNameExtensionFilter("OWL files", new String[]{"owl"});
		chooser.setFileFilter(fileFilter);
		int option = chooser.showOpenDialog(EntryPoint.frame);
		if (option == JFileChooser.APPROVE_OPTION) {
			uri = chooser.getSelectedFile().toURI();
			EntryPoint.loadOntologyFile(uri);
		} else {
		}
	}
}
