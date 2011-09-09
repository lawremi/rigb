package com.gene.bcb.rigb.genometry;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.affymetrix.genometryImpl.AnnotatedSeqGroup;
import com.affymetrix.genometryImpl.BioSeq;
import com.affymetrix.genometryImpl.SeqSpan;
import com.affymetrix.genometryImpl.SeqSymmetry;
import com.affymetrix.genometryImpl.span.SimpleSeqSpan;
import com.affymetrix.genometryImpl.symloader.SymLoader;
import com.affymetrix.genometryImpl.util.LoadUtils.LoadStrategy;
import com.gene.bcb.rigb.DataAccessException;
import com.gene.bcb.rigb.proxy.GRanges;

public class GRangesSymLoader extends SymLoader {
	private GRanges granges;
	private GRangesToSymmetries converter;

	public GRangesSymLoader(URI uri, String featureName, AnnotatedSeqGroup group) {
		super(uri, featureName, group);
		this.granges = lookupGRangesByURI(uri);
		this.converter = lookupConverterByURI(uri);
	}

	@Override
	public List<? extends SeqSymmetry> getChromosome(BioSeq seq) {
		return getRegion(new SimpleSeqSpan(1, seq.getLength(), seq));
	}

	@Override
	public List<BioSeq> getChromosomeList() {
		Map<String, Integer> seqlengths = Collections.emptyMap();
		try {
			seqlengths = granges.getSeqLengths();
		} catch (DataAccessException e) {
			Logger.getLogger(GRangesSymLoader.class.getName()).log(Level.SEVERE,
					"Failed to build chromosome list", e);
		}
		List<BioSeq> chromosomeList = new ArrayList<BioSeq>(seqlengths.size());
		for (Entry<String, Integer> seqlength : seqlengths.entrySet()) {
			chromosomeList.add(group.addSeq(seqlength.getKey(), seqlength.getValue()));
		}
		return chromosomeList;
	}

	@Override
	public List<? extends SeqSymmetry> getGenome() {
		return getSymmetries(granges);
	}

	@Override
	public List<? extends SeqSymmetry> getRegion(SeqSpan overlapSpan) {
		List<? extends SeqSymmetry> symmetries = Collections.emptyList();
		try {
			symmetries = getSymmetries(granges.subsetByOverlaps(overlapSpan));
		} catch (DataAccessException e) {
			Logger.getLogger(GRangesSymLoader.class.getName()).log(Level.SEVERE,
					"Failed to get region '" + overlapSpan + "'", e);
		}
		return symmetries;
	}

	@Override
	public List<LoadStrategy> getLoadChoices() {
		return Arrays.asList(LoadStrategy.NO_LOAD,
				             LoadStrategy.GENOME,
				             LoadStrategy.CHROMOSOME,
				             LoadStrategy.VISIBLE,
				             LoadStrategy.AUTOLOAD);
	}

	private List<? extends SeqSymmetry> getSymmetries(GRanges granges) {
		List <? extends SeqSymmetry> symmetries = Collections.emptyList();
		try {
			symmetries = converter.convert(granges, group);
		} catch (DataAccessException e) {
			Logger.getLogger(GRangesSymLoader.class.getName()).log(Level.SEVERE,
					"Failed to get features", e);
		}
		return symmetries;
	}

	private GRanges lookupGRangesByURI(URI uri) {
		// TODO Auto-generated method stub
		return null;
	}

	private GRangesToSymmetries lookupConverterByURI(URI uri) {
		// TODO Auto-generated method stub
		return null;
	}
}
