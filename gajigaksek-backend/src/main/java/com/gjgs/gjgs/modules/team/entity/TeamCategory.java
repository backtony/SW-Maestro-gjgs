package com.gjgs.gjgs.modules.team.entity;


import com.gjgs.gjgs.modules.category.entity.Category;
import com.gjgs.gjgs.modules.utils.base.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id", callSuper = false)
public class TeamCategory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TEAM_CATAGORY_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TEAM_ID", nullable = false)
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CATEGORY_ID", nullable = false)
    private Category category;

    public static TeamCategory of(Team team, Category category) {
        return TeamCategory.builder()
                .team(team)
                .category(category)
                .build();
    }

    public static List<TeamCategory> createTeamCategoryList(List<Category> categoryList, Team team) {
        return categoryList.stream()
                .map(category -> TeamCategory.of(team, category))
                .collect(toList());
    }
}
