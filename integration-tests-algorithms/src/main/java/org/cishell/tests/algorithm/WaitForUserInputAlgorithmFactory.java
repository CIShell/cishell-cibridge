package org.cishell.tests.algorithm;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.algorithm.ProgressMonitor;
import org.cishell.framework.algorithm.ProgressTrackable;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.service.guibuilder.GUIBuilderService;
import org.osgi.framework.FrameworkUtil;
import org.osgi.service.metatype.MetaTypeInformation;
import org.osgi.service.metatype.MetaTypeService;

import java.util.Dictionary;

public class WaitForUserInputAlgorithmFactory implements AlgorithmFactory {

    public Algorithm createAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        return new WaitForUserInputAlgorithm(data, parameters, context);
    }

    private class WaitForUserInputAlgorithm implements Algorithm, ProgressTrackable {

        private ProgressMonitor progressMonitor;
        private CIShellContext cishellContext;

        private WaitForUserInputAlgorithm(Data[] data, Dictionary parameters, CIShellContext cishellContext) {
            this.cishellContext = cishellContext;
        }

        @Override
        public Data[] execute() {

            GUIBuilderService guiBuilderService = (GUIBuilderService) cishellContext.getService(GUIBuilderService.class.getName());
            MetaTypeService metaTypeService = (MetaTypeService) cishellContext.getService(MetaTypeService.class.getName());

            MetaTypeInformation metaTypeInformation = metaTypeService.getMetaTypeInformation(FrameworkUtil.getBundle(this.getClass()));

            Dictionary response = guiBuilderService.createGUIandWait("org.cishell.tests.algorithm.WaitForUserInputAlgorithm", metaTypeInformation);
            assert response != null;
            assert response.get("ARandomNumber").equals("16");

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