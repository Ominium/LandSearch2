package com.example.landsearch.repository.maria

import com.example.landsearch.repository.maria.entity.CategoryEntity
import com.example.landsearch.repository.maria.entity.CommentEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CategoryRepository : JpaRepository<CategoryEntity, Long> {
}