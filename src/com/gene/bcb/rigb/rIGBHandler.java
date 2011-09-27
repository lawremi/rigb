package com.gene.bcb.rigb;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.HashMap;

import com.affymetrix.genometryImpl.AnnotatedSeqGroup;
import com.affymetrix.genometryImpl.parsers.FileTypeHandler;
import com.affymetrix.genometryImpl.parsers.IndexWriter;
import com.affymetrix.genometryImpl.parsers.Parser;
import com.affymetrix.genometryImpl.symloader.SymLoader;
import com.gene.bcb.rigb.genometry.GRangesSymLoader;
import com.gene.bcb.rigb.proxy.GRanges;

public class rIGBHandler implements FileTypeHandler {
	private static final String[] EXTENSIONS = new String[]{"rigb"};
    public HashMap<String, GRanges> idToGRanges =
        new HashMap<String, GRanges>();

	public rIGBHandler() {
		super();
	}

	@Override
	public String getName() {
		return "rIGB";
	}

	@Override
	public String[] getExtensions() {
		return EXTENSIONS;
	}

	public static List<String> getFormatPrefList() {
		return Arrays.asList(EXTENSIONS);
	}

	@Override
	public SymLoader createSymLoader(URI uri, String featureName,
			AnnotatedSeqGroup group) {
		SymLoader symLoader = new GRangesSymLoader(uri, featureName, group);
		return symLoader;
	}

	@Override
	public Parser getParser() {
		return null;
	}

	@Override
	public IndexWriter getIndexWriter(String stream_name) {
		return null;
	}
}
