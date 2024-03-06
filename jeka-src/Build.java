import dev.jeka.core.api.file.JkPathFile;
import dev.jeka.core.api.file.JkZipTree;
import dev.jeka.core.api.java.JkJavaProcess;
import dev.jeka.core.api.project.JkProject;
import dev.jeka.core.api.project.JkProjectSourceGenerator;
import dev.jeka.core.api.system.JkLog;
import dev.jeka.core.api.system.JkProcess;
import dev.jeka.core.api.utils.JkUtilsPath;
import dev.jeka.core.api.utils.JkUtilsString;
import dev.jeka.core.api.utils.JkUtilsZip;
import dev.jeka.core.tool.JkDoc;
import dev.jeka.core.tool.KBean;
import dev.jeka.core.tool.builtins.project.ProjectKBean;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

class Build extends KBean {

    public static final String COW_ZIP_URL = "https://github.com/ricksbrown/cowsay-perl/archive/master.zip";

    final JkProject project = load(ProjectKBean.class).project;

    @JkDoc("Creates a native image when packing")
    public boolean nativeImg;

    /*
     * Configures KBean project
     * When this method is called, option fields have already been injected from command line.
     */
    @Override
    protected void init() {
        project.compilation.postCompileActions.append("import-cow-files", this::copyCowFiles);
        project.packActions.appendIf(nativeImg, "Creates native image", this::nativeImg);
    }

    public void copyCowFiles() {
        Path target = project.compilation.layout.resolveClassDir().resolve("cows");
        JkUtilsZip.unzipUrl(COW_ZIP_URL, "/cowsay-perl-master/cows", target);
    }

    @JkDoc("Build a native Image")
    public void nativeImg() {
        Path javaHome = JkJavaProcess.CURRENT_JAVA_HOME;
        Path jar = project.artifactLocator.getMainArtifactPath();
        String relTarget = JkUtilsString.substringBeforeLast(jar.toString(), ".jar");
        Path target = Paths.get(relTarget).toAbsolutePath();
        Path nativeImageExe = javaHome.resolve("bin/native-image");
        JkProcess.of(nativeImageExe.toString(), "--no-fallback")
                .addParams("-H:+UnlockExperimentalVMOptions")
                .addParams("-H:IncludeResources=cows/.*$")
                .addParams("-H:IncludeResourceBundles=MessagesBundle")
                .addParams("-jar", jar.toString())
                .addParams("-H:Name=" + target)
                .setLogCommand(true)
                .setLogWithJekaDecorator(true)
                .exec();
        JkLog.info("Generated in %s", target);
        JkLog.info("Run: %s -f dragon Hello Jeka", relTarget);
    }

}