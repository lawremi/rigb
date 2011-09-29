package com.gene.bcb.rigb;

import java.awt.event.ActionEvent;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.util.tracker.ServiceTracker;

import com.affymetrix.genometryImpl.parsers.FileTypeHandler;
import com.affymetrix.genometryImpl.event.GenericAction;
import com.affymetrix.genoviz.swing.MenuUtil;
import com.affymetrix.genoviz.swing.recordplayback.JRPMenu;
import com.affymetrix.genoviz.swing.recordplayback.JRPMenuItem;
import com.affymetrix.igb.osgi.service.IGBService;
import com.affymetrix.igb.shared.OpenURIAction;
import com.gene.bcb.rigb.genometry.GRangesSymLoader;

public class Activator implements BundleActivator {
	private BundleContext bundleContext;
	private ServiceRegistration rIGBHandlerRegistration;
	private ServiceRegistration rIGBManagerRegistration;

	private void registerServices(IGBService igbService) {
		// rIGBHandlerRegistration = bundleContext.registerService(FileTypeHandler.class.getName(), new rIGBHandler(), new Properties());
        Properties metadata = new Properties();
        metadata.setProperty("name", "rIGB");
        rIGBHandlerRegistration =
            bundleContext.registerService(FileTypeHandler.class.getName(),
                                          new rIGBHandler(),
                                          metadata);
        rIGBManagerRegistration =
            bundleContext.registerService(rIGBManager.class.getName(),
                                          rIGBManager.getInstance(),
                                          new Properties());
            
		loadMenu();
	}

	private void loadMenu() {
		JRPMenu rigb_menu = MenuUtil.getRPMenu("rIGB_ids", "rIGB");
		for (String id : getGRangeCollectionIds()) {
			JRPMenuItem item = new JRPMenuItem("rIGB_" + id, getOpenIdAction(id));
			item.setText(id);
			rigb_menu.add(item);
		}
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

	private GenericAction getOpenIdAction(final String id) {
		return new OpenURIAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				super.actionPerformed(e);
				try {
					openURI(new URI(GRangesSymLoader.idToUri(id)));
				}
				catch (URISyntaxException x) {
					Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "could not load " + id, x);
				}
			}
			@Override
			public String getText() {
				return null;
			}
		};
	}

	// replace this stub with the real collection of GRange collection ids
	private List<String> getGRangeCollectionIds() {
		List<String> GRangeCollectionIds = new ArrayList<String>();
		GRangeCollectionIds.add("abc");
		GRangeCollectionIds.add("def");
		GRangeCollectionIds.add("xyz");
		return GRangeCollectionIds;
	}
}
