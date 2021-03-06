package org.easyb.eclipse.templates;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.content.IContentType;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class TemplateActivator extends AbstractUIPlugin {
	private String STORY_CONTENT_TYPE = "org.easyb.eclipse.contenttype.story";
	private String SPECIFICATION_CONTENT_TYPE = "org.easyb.eclipse.contenttype.specification";
	
	// The plug-in ID
	public static final String PLUGIN_ID = "org.easyb.eclipse.templates";

	// The shared instance
	private static TemplateActivator plugin;
	
	/**
	 * The constructor
	 */
	public TemplateActivator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static TemplateActivator getDefault() {
		return plugin;
	}
	
	public static void Log(String message){
		getDefault().getLog().log(new Status(IStatus.ERROR, PLUGIN_ID,message));
	}

	public static void Log(String message,Exception ex){
		getDefault().getLog().log(new Status(IStatus.ERROR, PLUGIN_ID,0,message,ex));
	}
	
	public static void Log(IStatus status){
		getDefault().getLog().log(status);
	}
	
	public boolean isBehaviourFile(IResource resource){
		if(resource.getType() != IResource.FILE){
			return false;
		}
		
		IContentType type = 
			Platform.getContentTypeManager().getContentType(STORY_CONTENT_TYPE);
		
		String resourceExt = ((IFile)resource).getFileExtension();
		if(isFileExtensionMatch(type.getFileSpecs(IContentType.FILE_EXTENSION_SPEC),resourceExt)){
			return true;
		}
		
		type = 
			Platform.getContentTypeManager().getContentType(SPECIFICATION_CONTENT_TYPE);

		if(isFileExtensionMatch(type.getFileSpecs(IContentType.FILE_EXTENSION_SPEC),resourceExt)){
			return true;
		}
		
		return false;
	}
	
	private boolean isFileExtensionMatch(String[] extensions,String resourceExt){
		for(String exten : extensions){
			if(exten.equals(resourceExt)){
				return true;
			}
		}
		
		return false;
	}
}
