package org.boinq.web.rest.dto;

import javax.annotation.Nullable;

import org.boinq.domain.query.ReferenceMapEntry;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class ReferenceMapEntryDTO {

	public static ReferenceMapEntryDTO create(ReferenceMapEntry entry) {
		return create(entry.getId(), entry.getBoinqReferenceURI(), entry.getRemoteReferenceURI());
	}
	
	@JsonCreator public static ReferenceMapEntryDTO create(
			@JsonProperty("id")										Long id,
			@JsonProperty("boinqReferenceURI")						String boinqReferenceURI,
			@JsonProperty("remoteReferenceURI")						String remoteReferenceURI) {
		return new AutoValue_ReferenceMapEntryDTO(id, boinqReferenceURI, remoteReferenceURI);
	}
	
	@JsonProperty("id") @Nullable							public abstract Long id();
	@JsonProperty("remoteReferenceURI")						public abstract String boinqReferenceURI();
	@JsonProperty("boinqReferenceURI")						public abstract String remoteReferenceURI();
	
}
