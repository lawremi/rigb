package com.gene.bcb.rigb;

import java.util.HashMap;
import java.net.URI;
import java.net.URISyntaxException;
import java.lang.reflect.InvocationTargetException;

import com.affymetrix.igb.shared.OpenURIAction;
import org.rosuda.REngine.REXPReference;
import org.rosuda.REngine.REngine;
import org.rosuda.REngine.REngineException;
import org.rosuda.REngine.REngineStdOutput;
import org.rosuda.REngine.JRI.JRIEngine;

import com.affymetrix.igb.IGBServiceImpl;
import com.gene.bcb.rigb.proxy.GRanges;

public class rIGBManager {
    public final static rIGBManager INSTANCE = new rIGBManager();

    public HashMap<String, GRanges> idToGRanges =
        new HashMap<String, GRanges>();

    private rIGBManager() {
    }

    public static rIGBManager getInstance() {
        return INSTANCE;
    }

    // public void putGRanges(String id, REXPReference granges) {
    //     this.idToGRanges.put(id, new GRanges(granges));
    // }

    // public void putGRanges(String id, Object granges) {
    //     System.out.println("class " + granges.getClass().toString());
    //     System.out.println("first " + granges.getClass().getClassLoader().getClass().toString());
    //     System.out.println("second " + new GRanges(null).getClass().getClassLoader().getClass().toString());
    //     this.idToGRanges.put(id, new GRanges((REXPReference) granges));
    // }

    public void putGRanges() throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, REngineException {
        // REngine engine = REngine.engineForClass("org.rosuda.REngine.JRI.JRIEngine");
        // REngine engine = JRIEngine.createEngine(new String[] {"--vanilla", "--slave"},
        //                                         new REngineStdOutput(),
        //                                         false);

        REngine engine = new org.rosuda.REngine.JRI. JRIEngine(new org.rosuda.JRI.Rengine());

        // You're not going to see this; the above blocks.
        System.out.println(engine.toString());
    }

    public void openURI(final String uri) throws URISyntaxException {
        new OpenURIAction(IGBServiceImpl.getInstance()) {
            public String getText() {
                return uri;
            }            
            {
                openURI(new URI(uri));
                // try {
                // } catch (URISyntaxException e) {
                //     e.printStackTrace();
                // }
            }
        };
    }
}
