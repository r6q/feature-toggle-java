package com.github.r6q.featuretoggle.components.feature;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Optional;

@Mapper
interface FeatureMapper {

    @Select("SELECT * FROM feature")
    List<FeatureEntity> getAll();

    @Select("SELECT * FROM feature WHERE id = #{id}")
    Optional<FeatureEntity> getById(@Param("id") int id);

    @Select("""
INSERT INTO feature (name, description, enabled, expiration)
VALUES (#{name}, #{description}, #{enabled}, #{expiration})
returning id
""")
    int create(FeatureEntity entity);

    @Update("""
UPDATE feature
SET name = #{name}, description = #{description}, enabled = #{enabled}, expiration = #{expiration}
WHERE id = #{id}
""")
    void update(FeatureEntity entity);

    @Delete("DELETE FROM feature WHERE id = #{id}")
    void delete(int id);
}
