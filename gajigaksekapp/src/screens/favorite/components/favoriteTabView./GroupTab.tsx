import React from 'react';
import {StyleSheet, View} from 'react-native';
import Icon from 'react-native-vector-icons/Ionicons';
import DropDownPicker from 'react-native-dropdown-picker';
import GroupApiController from '../../../../services/apis/GroupApiController';
import UserDC from '../../../../services/login/UserDC';
import FavoriteApiController from '../../../../services/apis/FavoriteApiController';
import LectureList from '@/components/LectureList';

interface GroupTabProps {
  navigation: any;
}

interface GroupTabStates {
  data: any;
  dropDowns: number[];
  groupDropDown: boolean;
  selectedGroup: number;
  teamList: any;
}

export default class GroupTab extends React.Component<
  GroupTabProps,
  GroupTabStates
> {
  constructor(props: any) {
    super(props);
    this.state = {
      data: [],
      dropDowns: [],
      groupDropDown: false,
      selectedGroup: 0,
      teamList: [],
    };
    this.getTeamList();
  }

  private async getTeamList() {
    if (UserDC.isLogout()) {
      return;
    }
    try {
      const res = await GroupApiController.getTeamList({});
      this.setState({teamList: res?.data.myTeamList});
    } catch (e) {
      console.error(e);
    }
  }

  private async getTeamLecture() {
    if (this.state.selectedGroup === 0) {
      this.setState({data: []});
      return;
    }
    try {
      const res = await FavoriteApiController.getTeamLecture({
        teamId: this.state.selectedGroup,
      });
      this.setState({data: res.data.lectureTeamDtoList});
    } catch (e) {
      console.error(e);
    }
  }

  private async deleteTeamLecture(lectureId: number) {
    try {
      await FavoriteApiController.deleteTeamLecture({
        teamId: this.state.selectedGroup,
        lectureId: lectureId,
      });
      this.getTeamLecture();
    } catch (e) {
      console.error(e);
    }
  }

  render() {
    return (
      <View style={this.styles.container}>
        <View style={this.styles.innerContainer}>
          <View
            style={[
              this.styles.dropDownWrapper,
              this.state.groupDropDown ? {height: 500} : {},
            ]}>
            <DropDownPicker
              open={this.state.groupDropDown}
              value={this.state.selectedGroup}
              items={
                this.state.teamList
                  ? [
                      {
                        label: '그룹 선택',
                        value: 0,
                        icon: () => (
                          <Icon name="add" size={12} color="#4f6cff" />
                        ),
                      },
                      ...this.state.teamList.map(item => ({
                        label: item.teamName,
                        value: item.teamId,
                      })),
                    ]
                  : [
                      {
                        label: '그룹 선택',
                        value: 0,
                        icon: () => (
                          <Icon name="add" size={12} color="#4f6cff" />
                        ),
                      },
                    ]
              }
              setOpen={() => {
                this.setState({groupDropDown: !this.state.groupDropDown});
              }}
              setValue={callback => {
                this.setState(
                  state => ({
                    selectedGroup: callback(state.selectedGroup),
                  }),
                  () => {
                    this.getTeamLecture();
                  },
                );
              }}
              setItems={() => {}}
              showTickIcon={false}
              style={this.styles.dropDown}
              listItemContainerStyle={this.styles.listItemContainer}
              listItemLabelStyle={this.styles.listItemLabel}
              dropDownContainerStyle={this.styles.dropDownContainer}
              arrowIconStyle={this.styles.arrowIcon}
              textStyle={this.styles.dropDownText}
            />
          </View>

          <LectureList
            navigation={this.props.navigation}
            data={this.state.data}
            refreshParent={() => this.getTeamLecture()}
            deleteLecture={(lectureId: number) =>
              this.deleteTeamLecture(lectureId)
            }
          />
        </View>
      </View>
    );
  }

  private styles = StyleSheet.create({
    container: {flex: 1, backgroundColor: '#FFF'},
    innerContainer: {backgroundColor: '#FFF', flex: 1, marginTop: 10},
    dropDownWrapper: {
      paddingHorizontal: 25,
      paddingVertical: 10,
      zIndex: 100,
    },
    listItemContainer: {
      height: 30,
      backgroundColor: '#fafafb',
    },
    dropDown: {
      backgroundColor: '#fafafb',
      borderWidth: 0,
      height: 34,
      borderRadius: 6,
      flexDirection: 'row',
      justifyContent: 'space-between',
      alignItems: 'center',
    },
    listItemLabel: {color: '#4a4a4c', fontSize: 12},
    dropDownContainer: {
      borderWidth: 0,
    },
    arrowIcon: {
      width: 18,
      height: 18,
    },
    dropDownText: {fontSize: 14},
  });
}
