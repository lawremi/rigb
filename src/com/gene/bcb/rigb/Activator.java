package com.gene.bcb.rigb;

import java.net.URI;
import java.util.Properties;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.util.tracker.ServiceTracker;

import com.affymetrix.genometryImpl.GenometryModel;
import com.affymetrix.genometryImpl.parsers.FileTypeHandler;
import com.affymetrix.igb.osgi.service.IGBService;

public class Activator implements BundleActivator {
	private BundleContext bundleContext;
	private ServiceRegistration rIGBHandlerRegistration;

	private void registerServices(IGBService igbService) {
		rIGBHandlerRegistration = bundleContext.registerService(FileTypeHandler.class.getName(), new rIGBHandler(), new Properties());
	}

	@Override
	public void start(BundleContext bundleContext_) throws Exception {
		this.bundleContext = bundleContext_;
    	ServiceReference igbServiceReference = bundleContext.getServiceReference(IGBService.class.getName());

        if (igbServiceReference != null)
        {
        	IGBService igbService = (IGBService) bundleContext.getService(igbServiceReference);
        	registerServices(igbService);
        }
        else
        {
        	ServiceTracker serviceTracker = new ServiceTracker(bundleContext, IGBService.class.getName(), null) {
        	    public Object addingService(ServiceReference igbServiceReference) {
                	IGBService igbService = (IGBService) bundleContext.getService(igbServiceReference);
                   	registerServices(igbService);
                    return super.addingService(igbServiceReference);
        	    }
        	};
        	serviceTracker.open();
        }
    }

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		rIGBHandlerRegistration.unregister();
	}
}
