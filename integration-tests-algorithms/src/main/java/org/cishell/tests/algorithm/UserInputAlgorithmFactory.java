package org.cishell.tests.algorithm;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.algorithm.ProgressMonitor;
import org.cishell.framework.algorithm.ProgressTrackable;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.service.guibuilder.GUIBuilderService;
import org.osgi.service.metatype.MetaTypeProvider;

import java.util.Dictionary;

public class UserInputAlgorithmFactory implements AlgorithmFactory {

    public Algorithm createAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        return new UserInputAlgorithm(data, parameters, context);
    }

    private class UserInputAlgorithm implements Algorithm, ProgressTrackable {

        private ProgressMonitor progressMonitor;
        private CIShellContext cishellContext;

        private UserInputAlgorithm(Data[] data, Dictionary parameters, CIShellContext cishellContext) {
            this.cishellContext = cishellContext;
        }

        @Override
        public Data[] execute() {

            GUIBuilderService guiBuilderService = (GUIBuilderService) cishellContext.getService(GUIBuilderService.class.getName());
            MetaTypeProvider metaTypeProvider = (MetaTypeProvider) cishellContext.getService(MetaTypeProvider.class.getName());

            guiBuilderService.createGUIandWait("org.cishell.tests.algorithm.UserInputAlgorithm" , metaTypeProvider);

            return new BasicData[0];
        }

        @Override
        public void setProgressMonitor(ProgressMonitor progressMonitor) {
            this.progressMonitor = progressMonitor;
        }

        @Override
        public ProgressMonitor getProgressMonitor() {
            return progressMonitor;
        }
    }
}