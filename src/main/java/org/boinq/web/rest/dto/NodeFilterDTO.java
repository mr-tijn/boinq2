package org.boinq.web.rest.dto;

import java.util.List;

import javax.annotation.Nullable;

import org.boinq.domain.query.NodeFilter;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.boinq.web.rest.dto.AutoValue_NodeFilterDTO;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class NodeFilterDTO { 
	public static NodeFilterDTO create(NodeFilter filter) {
		return create(
				filter.getId(), 
				filter.getType(), 
				filter.getCaseInsensitive(), 
				filter.getNot(), 
				filter.getExactMatch(),
				filter.getMinInteger(), 
				filter.getMaxInteger(), 
				filter.getIntegerValue(), 
				filter.getMinDouble(),
				filter.getMaxDouble(),
				filter.getDoubleValue(),
				filter.getIncludeMin(),
				filter.getIncludeMax(),
				filter.getStringValue(),
				filter.getTermValues(),
				filter.getContig(),
				filter.getStrand());
	}
	
	@JsonCreator public static NodeFilterDTO create(
		    @JsonProperty("id") 					Long id,

			@JsonProperty("type") 							int type,
			@JsonProperty("caseInsensitive") @Nullable		Boolean caseInsensitive,
			@JsonProperty("not")							Boolean not,
			@JsonProperty("exactMatch")						Boolean exactMatch,
			
			@JsonProperty("minInteger") @Nullable			Long minInteger,
			@JsonProperty("maxInteger") @Nullable			Long maxInteger,
			@JsonProperty("integerValue") @Nullable			Long integerValue,
			@JsonProperty("minDouble") @Nullable			Double minDouble,
			@JsonProperty("maxDouble") @Nullable			Double maxDouble,
			@JsonProperty("doubleValue") @Nullable			Double doubleValue,
			@JsonProperty("includeMin") @Nullable			Boolean includeMin,
			@JsonProperty("includeMax") @Nullable			Boolean includeMax,
			
			@JsonProperty("stringValue") @Nullable			String stringValue,

			@JsonProperty("termValues") @Nullable			List<String> termValues,
			
		    @JsonProperty("contig") @Nullable				String contig,
		    @JsonProperty("strand") @Nullable				Boolean strand) {
		return new AutoValue_NodeFilterDTO(id, type, caseInsensitive, not, exactMatch, minInteger, maxInteger, integerValue, minDouble, maxDouble, doubleValue, includeMin, includeMax, stringValue, termValues, contig, strand);
	}
	
	
    @JsonProperty("id") @Nullable					public abstract Long id();

	@JsonProperty("type") 							public abstract int type();
	@JsonProperty("caseInsensitive") @Nullable		public abstract Boolean caseInsensitive();
	@JsonProperty("not")							public abstract Boolean not();
	@JsonProperty("exactMatch")						public abstract Boolean exactMatch();
	
	@JsonProperty("minInteger") @Nullable			public abstract Long minInteger();
	@JsonProperty("maxInteger") @Nullable			public abstract Long maxInteger();
	@JsonProperty("integerValue") @Nullable			public abstract Long integerValue();
	@JsonProperty("minDouble") @Nullable			public abstract Double minDouble();
	@JsonProperty("maxDouble") @Nullable			public abstract Double maxDouble();
	@JsonProperty("doubleValue") @Nullable			public abstract Double doubleValue();
	@JsonProperty("includeMin") @Nullable			public abstract Boolean includeMin();
	@JsonProperty("includeMax") @Nullable			public abstract Boolean includeMax();
	
	@JsonProperty("stringValue") @Nullable			public abstract String stringValue();

	@JsonProperty("termValues") @Nullable 			public abstract List<String> termValues();
	
    @JsonProperty("contig") @Nullable				public abstract String contig();
    @JsonProperty("strand") @Nullable				public abstract Boolean strand();

	
}
