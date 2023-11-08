package com.example.landsearch.repository.maria.entity

import com.querydsl.core.annotations.QueryEntity
import lombok.AllArgsConstructor
import lombok.NoArgsConstructor
import org.hibernate.annotations.DynamicInsert
import org.hibernate.annotations.DynamicUpdate
import javax.persistence.*

@QueryEntity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "MAINCATEGORY")
@DynamicInsert
@DynamicUpdate
@Cacheable
@Entity
class CategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="main_seq")
    var mainSeq : Long?=  null
    @Column(name="codenumber")
    var codeNumber : String? = null
    @Column(name="codename")
    var codeName: String? =null
}