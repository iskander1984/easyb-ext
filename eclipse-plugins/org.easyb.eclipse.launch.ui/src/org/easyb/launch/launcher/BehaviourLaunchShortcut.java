package org.easyb.launch.launcher;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jdt.groovy.model.GroovyCompilationUnit;
import org.easyb.launch.EasybLaunchActivator;
import org.easyb.launch.ILaunchConstants;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.pde.ui.launcher.AbstractLaunchShortcut;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;

/**
 * Shortcut used to launch a Easyb story from project explorer
 * Will only launch on fields ending with *.story as defined
 * in the plugin.xml 
 * @author whiteda
 *
 */
public class BehaviourLaunchShortcut extends AbstractLaunchShortcut{
	private IFile file=null;
	
	@Override
	protected String getLaunchConfigurationTypeName() {
		return BehaviourLaunchConfigurationDelegate.ID;
	}

	@Override
	protected void initializeConfiguration(ILaunchConfigurationWorkingCopy wc) {
		setConfigFileFullPath(wc);
		setConfigProject(wc);
		setConfigFileProjectPath(wc);
	}

	@Override
	protected boolean isGoodMatch(ILaunchConfiguration config){
		
		try {
			List<String> stories = config.getAttribute(
					ILaunchConstants.LAUNCH_ATTR_STORIES_FULL_PATH,
					new ArrayList<String>(0));

			if (stories.size() == 0 || stories.size()>1)  {
				return false;
			}
			
			String filePath = file.getRawLocation().toOSString();
			if(stories.get(0).equals(filePath)){
				return true;
			}

		} catch (CoreException ce) {
			EasybLaunchActivator.Log("Unable to get stories for launch", ce);
		}
		return false;
	}

	@Override
	public void launch(IEditorPart editor, String mode) {
		IEditorInput input = editor.getEditorInput();
		
		if (input instanceof IFileEditorInput) {
			IFileEditorInput fileInput = (IFileEditorInput)input;
			file =fileInput.getFile();
			launch(mode);
		
		}	
	}

	@Override
	public void launch(ISelection selection, String mode) {
		Object[] selectArr = null;
		if (selection instanceof IStructuredSelection) {
			selectArr = ((IStructuredSelection)selection).toArray();
			
			try {
				if(selectArr.length>0){
					
					if(selectArr[0] instanceof IFile){
						file = (IFile)selectArr[0];
					}else if(selectArr[0] instanceof GroovyCompilationUnit){
						//TODO change to EasybCompilationUnit when its accessable
						GroovyCompilationUnit compUnit = (GroovyCompilationUnit)selectArr[0];
					
						IResource resource = compUnit.getCorrespondingResource();
						
						if(resource.getType() == IResource.FILE){
							file = (IFile)resource;
						}
					}
					
				}
			} catch (JavaModelException e) {
				EasybLaunchActivator.Log("Unable to get the behaviour file for launch",e);
			}

			launch(mode);
        } 
	}
	
	private void setConfigFileFullPath(ILaunchConfigurationWorkingCopy wc){
		String filePath = file.getRawLocation().toOSString();
		List<String> stories = new ArrayList<String>();
		stories.add(filePath);
		
		wc.setAttribute(ILaunchConstants.LAUNCH_ATTR_STORIES_FULL_PATH,stories);
	}
	
	private void setConfigProject(ILaunchConfigurationWorkingCopy wc){
		IProject proj = file.getProject();
		
		if(proj!=null){
			wc.setAttribute(
					IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME,proj.getName());
		}
	}
	
	private void setConfigFileProjectPath(ILaunchConfigurationWorkingCopy wc){
		wc.setAttribute(
				ILaunchConstants.LAUNCH_ATTR_STORY_PATH,file.getProjectRelativePath().toPortableString());
	}

}
