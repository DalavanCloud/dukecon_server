package org.dukecon.server.model

import groovy.transform.TypeChecked
import groovy.transform.builder.Builder

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ElementCollection

/**
 * @author Falk Sippach, falk@jug-da.de, @sippsack
 */
@Builder
@TypeChecked
@Entity(name = "filters")
class Filters {
    @Id
    @GeneratedValue
    long id

    @Column(name = "principal_id", nullable = false)
    private String principalId

    private boolean favourites = false

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> languages = []

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> levels = []

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> tracks = []

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> rooms = []
}
