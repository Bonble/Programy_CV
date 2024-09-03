package com.example.projekt.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jdk.jfr.Recording;
import lombok.*;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable=false, unique=true)
    private String login;

    @Column(nullable=false)
    private String password;
    @ManyToMany(cascade=CascadeType.ALL, fetch = FetchType.EAGER)
    @LazyCollection(LazyCollectionOption.FALSE)
    private Collection<Role> roles;

    // fetch = FetchType.EAGER
    @OneToMany(mappedBy = "user_id", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<LokataAktywna> lokatyAktywne = new HashSet<>();

            /*@OneToOne(fetch = FetchType.LAZY)
            @JoinTable( name = "newsletter", joinColumns = @JoinColumn ( name = "question_id"), inverseJoinColumns = @JoinColumn( name = "option_id"))
            private Collection<ProduktItem> produktItems;*/

    public Collection<Role> getRoles() {
        return roles;
    }
    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }

    /*
    public Set<ProduktItem> getNewsletter() {
        return produktItems;
    }

    public void setNewsletter(Set<ProduktItem> newsletters) {
        this.produktItems = newsletters;
    }*/



}