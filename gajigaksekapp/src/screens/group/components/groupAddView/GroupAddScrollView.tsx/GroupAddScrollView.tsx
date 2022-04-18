import React from 'react';
import {ScrollView, Text, TextInput, View} from 'react-native';
import GroupNameBlock from './GroupNameBlock';
import GroupDayBlock from './GroupDayBlock';
import GroupTimeBlock from './GroupTimeBlock';
import GroupPeopleBlock from './GroupPeopleBlock';
import GroupZoneBlock from './GroupZoneBlock';
import GroupCategoryBlock from './GroupCategoryBlock';
import {StyleSheet} from 'react-native';

interface GroupAddScrollViewProps {
  navigation: any;
  title: string;
  clickedDayOfWeek: string[];
  time: string[];
  people: number;
  modalVisible: boolean;
  categoryModalVisible: boolean;
  subZone: string;
  subCategory: number[];
  setTitle: (title: string) => void;
  onClickBigDayOfWeek: (bigDay: string) => void;
  onClickDayOfWeek: (day: string) => void;
  bigDayChecked: (bigDay: string) => boolean;
  onClickTime: (time: string) => void;
  onClickTimeBig: () => void;
  setPeople: (people: number) => void;
  setModalVisible: (modalVisible: boolean) => void;
  setCategoryModalVisible: (categoryModalVisible: boolean) => void;
  setSubCategory: (idList: number[]) => void;
}

interface GroupAddScrollViewStates {}

export default class GroupAddScrollView extends React.Component<
  GroupAddScrollViewProps,
  GroupAddScrollViewStates
> {
  render() {
    return (
      <ScrollView style={this.styles.scrollView}>
        <GroupNameBlock
          title={this.props.title}
          setTitle={(title: string) => this.props.setTitle(title)}
        />
        <Text style={this.styles.text}>그룹 요약</Text>
        <GroupDayBlock
          clickedDayOfWeek={this.props.clickedDayOfWeek}
          onClickBigDayOfWeek={(bigDay: string) =>
            this.props.onClickBigDayOfWeek(bigDay)
          }
          bigDayChecked={(bigDay: string) => this.props.bigDayChecked(bigDay)}
          onClickDayOfWeek={(day: string) => this.props.onClickDayOfWeek(day)}
        />
        <GroupTimeBlock
          onClickTime={(time: string) => this.props.onClickTime(time)}
          onClickTimeBig={() => this.props.onClickTimeBig()}
          time={this.props.time}
        />
        <GroupPeopleBlock
          people={this.props.people}
          setPeople={(people: number) => this.props.setPeople(people)}
        />
        <GroupZoneBlock
          modalVisible={this.props.modalVisible}
          subZone={this.props.subZone}
          setModalVisible={(modalVisible: boolean) =>
            this.props.setModalVisible(modalVisible)
          }
        />
        <GroupCategoryBlock
          subCategory={this.props.subCategory}
          categoryModalVisible={this.props.categoryModalVisible}
          setCategoryModalVisible={(categoryModalVisible: boolean) =>
            this.props.setCategoryModalVisible(categoryModalVisible)
          }
        />
      </ScrollView>
    );
  }
  private styles = StyleSheet.create({
    scrollView: {paddingHorizontal: 20},
    text: {
      fontSize: 16,
      lineHeight: 18,
      fontFamily: 'NotoSansCJKkr-Bold',
      marginTop: 20,
      marginBottom: 10,
    },
  });
}
