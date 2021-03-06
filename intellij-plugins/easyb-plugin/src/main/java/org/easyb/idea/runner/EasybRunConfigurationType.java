package org.easyb.idea.runner;

import javax.swing.*;

import com.intellij.execution.LocatableConfigurationType;
import com.intellij.execution.Location;
import com.intellij.execution.RunManager;
import com.intellij.execution.RunnerAndConfigurationSettings;
import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.util.Comparing;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiClass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.plugins.groovy.lang.psi.GroovyFile;

/**
 * Adds an easyb run configuration to IntelliJ
 */
public class EasybRunConfigurationType implements LocatableConfigurationType {
    private EasybConfigurationFactory factory;

    public EasybRunConfigurationType() {
        factory = new EasybConfigurationFactory(this);
    }

    public String getDisplayName() {
        return "Easyb Specification";
    }

    public String getConfigurationTypeDescription() {
        return "Easyb Specification";
    }

    public Icon getIcon() {
        return IconLoader.getIcon("/easyb.png");
    }

    @NotNull
    public String getId() {
        return "EasybRunConfigurationType";
    }

    public ConfigurationFactory[] getConfigurationFactories() {
        return new ConfigurationFactory[]{factory};
    }

    public RunnerAndConfigurationSettings createConfigurationByLocation(Location location) {
        final PsiElement element = location.getPsiElement();
        final PsiFile file = element.getContainingFile();
        if (file == null) {
            return null;
        }
        return createConfiguration(file);
    }

    @SuppressWarnings({"SimplifiableIfStatement"})
    public boolean isConfigurationByLocation(RunConfiguration configuration, Location location) {
        final PsiClass aClass = getSpecClass(location.getPsiElement());
        if (aClass == null) return false;
        final VirtualFile vFile = aClass.getContainingFile().getVirtualFile();
        if (vFile == null) return false;
        return Comparing.equal(((EasybRunConfiguration) configuration).getSpecificationPath(), vFile.getPath());
    }

    private PsiClass getSpecClass(PsiElement element) {
        final PsiFile file = element.getContainingFile();
        if (!(file instanceof GroovyFile)) return null;
        return ((GroovyFile) file).getScriptClass();
    }

    private RunnerAndConfigurationSettings createConfiguration(final PsiFile easybSpecFile) {
        final Project project = easybSpecFile.getProject();
        RunnerAndConfigurationSettings settings = RunManager.getInstance(project).
                createRunConfiguration(easybSpecFile.getName(), factory);
        final EasybRunConfiguration configuration = (EasybRunConfiguration) settings.getConfiguration();
        final PsiDirectory dir = easybSpecFile.getContainingDirectory();
        assert dir != null;
        final VirtualFile vFile = easybSpecFile.getVirtualFile();
        assert vFile != null;
        configuration.setSpecificationPath(vFile.getPath());
        configuration.setModule(ModuleUtil.findModuleForPsiElement(easybSpecFile));
        return settings;
    }
}
