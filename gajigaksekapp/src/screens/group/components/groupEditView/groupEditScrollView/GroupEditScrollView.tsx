import React from 'react';
import {StyleSheet} from 'react-native';
import {ScrollView, Text} from 'react-native';
import GroupEditCategoryBlock from './GroupEditCategoryBlock';
import GroupEditDayBlock from './GroupEditDayBlock';
import GroupEditLeaderBlock from './GroupEditLeaderBlock';
import GroupEditPeopleBlock from './GroupEditPeopleBlock';
import GroupEditTimeBlock from './GroupEditTimeBlock';
import GroupEditTitleBlock from './GroupEditTitleBlock';
import GroupEditZoneBlock from './GroupEditZoneBlock';

interface GroupEditScrollViewProps {
  navigation: any;
  title: string;
  setTitle: (title: string) => void;
  memberList: any[];
  clickedMember: string;
  onClickMember: (nickname: string) => void;
  onClickBigDayOfWeek: (bigDay: string) => void;
  bigDayChecked: (bigDay: string) => boolean;
  clickedDayOfWeek: string[];
  onClickDayOfWeek: (day: string) => void;
  onClickTime: (time: string) => void;
  time: string[];
  onClickTimeBig: () => void;
  people: string;
  setPeople: (people: string) => void;
  subZone: string;
  setSubZone: (subZone: string) => void;
  subCategory: number[];
  setSubCategory: (idList: number[]) => void;
  onClickDelegate: () => void;
}

export default class GroupEditScrollView extends React.Component<
  GroupEditScrollViewProps,
  {modalVisible: boolean}
> {
  constructor() {
    super();
    this.state = {modalVisible: false};
  }

  render() {
    return (
      <ScrollView style={{paddingHorizontal: 20}}>
        <GroupEditTitleBlock
          title={this.props.title}
          setTitle={(title: string) => this.props.setTitle(title)}
        />
        <GroupEditLeaderBlock
          memberList={this.props.memberList}
          clickedMember={this.props.clickedMember}
          onClickMember={(nickname: string) =>
            this.props.onClickMember(nickname)
          }
          onClickDelegate={() => this.props.onClickDelegate()}
        />

        <Text style={this.styles.text}>그룹 요약 수정</Text>
        <GroupEditDayBlock
          clickedDayOfWeek={this.props.clickedDayOfWeek}
          onClickBigDayOfWeek={(bigDay: string) =>
            this.props.onClickBigDayOfWeek(bigDay)
          }
          bigDayChecked={(bigDay: string) => this.props.bigDayChecked(bigDay)}
          onClickDayOfWeek={(day: string) => this.props.onClickDayOfWeek(day)}
        />

        <GroupEditTimeBlock
          time={this.props.time}
          onClickTime={(time: string) => this.props.onClickTime(time)}
          onClickTimeBig={() => this.props.onClickTimeBig()}
        />

        <GroupEditPeopleBlock
          people={this.props.people}
          setPeople={(people: string) => this.props.setPeople(people)}
        />

        <GroupEditZoneBlock
          subZone={this.props.subZone}
          setSubZone={(sub: string) => this.props.setSubZone(sub)}
        />

        <GroupEditCategoryBlock
          navigation={this.props.navigation}
          subCategory={this.props.subCategory}
          setSubCategory={(idList: number[]) =>
            this.props.setSubCategory(idList)
          }
        />
      </ScrollView>
    );
  }

  private styles = StyleSheet.create({
    text: {
      fontSize: 16,
      lineHeight: 18,
      fontFamily: 'NotoSansCJKkr-Bold',
      marginTop: 20,
      marginBottom: 10,
    },
  });
}
