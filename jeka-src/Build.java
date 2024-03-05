import dev.jeka.core.api.file.JkPathFile;
import dev.jeka.core.api.file.JkZipTree;
import dev.jeka.core.api.project.JkProject;
import dev.jeka.core.api.project.JkProjectSourceGenerator;
import dev.jeka.core.api.utils.JkUtilsPath;
import dev.jeka.core.api.utils.JkUtilsZip;
import dev.jeka.core.tool.KBean;
import dev.jeka.core.tool.builtins.project.ProjectKBean;

import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

class Build extends KBean {

    public static final String COW_ZIP_URL = "https://github.com/ricksbrown/cowsay-perl/archive/master.zip";

    final JkProject project = load(ProjectKBean.class).project;

    /*
     * Configures KBean project
     * When this method is called, option fields have already been injected from command line.
     */
    @Override
    protected void init() {
        project.compilation.postCompileActions.append("import-cow-files", this::copyCowFiles);
    }

    public void copyCowFiles() {
        Path target = project.compilation.layout.resolveClassDir().resolve("cows");
        JkUtilsZip.unzipUrl(COW_ZIP_URL, "/cowsay-perl-master/cows", target);
    }

}