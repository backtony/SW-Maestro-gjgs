import GroupApiController from '@/services/apis/GroupApiController';
import UserDC from '@/services/login/UserDC';
import React from 'react';
import {StyleSheet, View} from 'react-native';
import DropDownPicker from 'react-native-dropdown-picker';
import Icon from 'react-native-vector-icons/Ionicons';

interface BulletinLectureSelectGroupTabDropDownProps {
  onChangeGroup: (teamId: number) => void;
}

interface BulletinLectureSelectGroupTabDropDownStates {
  groupDropDown: boolean;
  selectedGroup: number;
  teamList: any;
}

export default class BulletinLectureSelectGroupTabDropDown extends React.Component<
  BulletinLectureSelectGroupTabDropDownProps,
  BulletinLectureSelectGroupTabDropDownStates
> {
  constructor(props: any) {
    super(props);
    this.state = {groupDropDown: false, selectedGroup: 0, teamList: []};
    this.getGroupListApi();
  }

  private async getGroupListApi() {
    if (UserDC.isLogout()) {
      alert('로그인을 시도해주세요.');
      return;
    }
    try {
      const res = await GroupApiController.getTeamList({});
      this.setState({teamList: res?.data.myTeamList});
    } catch (e) {
      console.error(e);
    }
  }

  render() {
    return (
      <View
        style={[
          this.styles.dropDownWrapper,
          this.state.groupDropDown ? {height: 500} : {},
        ]}>
        <DropDownPicker
          open={this.state.groupDropDown}
          value={this.state.selectedGroup}
          items={[
            {
              label: '그룹 선택',
              value: 0,
              icon: () => <Icon name="add" size={12} color="#4f6cff" />,
            },
            ...this.state.teamList.map(item => ({
              label: item.teamName,
              value: item.teamId,
            })),
          ]}
          setOpen={() => {
            this.setState({groupDropDown: !this.state.groupDropDown});
          }}
          setValue={callback => {
            this.setState(
              state => ({
                selectedGroup: callback(state.selectedGroup),
              }),
              () => {
                this.props.onChangeGroup(this.state.selectedGroup);
              },
            );
          }}
          setItems={() => {}}
          showTickIcon={false}
          style={this.styles.dropDown}
          listItemContainerStyle={this.styles.listItemContainer}
          listItemLabelStyle={{color: '#4a4a4c', fontSize: 12}}
          dropDownContainerStyle={this.styles.dropDownContainer}
          arrowIconStyle={this.styles.arrowIcon}
          textStyle={{fontSize: 14}}
        />
      </View>
    );
  }
  private styles = StyleSheet.create({
    list: {
      paddingHorizontal: 10,
    },
    listContainer: {
      marginHorizontal: 10,
      marginTop: 10,
    },
    separator: {
      marginTop: 10,
    },
    /******** card **************/
    card: {
      backgroundColor: 'white',
      width: '45%',
      marginHorizontal: 10,
      marginBottom: 15,
    },
    cardContent: {
      paddingTop: 7,
      paddingLeft: 4,
      justifyContent: 'space-between',
    },
    cardImage: {
      flex: 1,
      height: 122,
      borderRadius: 9,
    },
    imageContainer: {
      borderRadius: 10,
    },
    /******** card components **************/
    zone: {
      fontSize: 10,
      color: '#FFF',
    },
    title: {
      fontFamily: 'NotoSansCJKkr-Bold',
      fontSize: 14,
      lineHeight: 16,
      color: '#070707',
      marginBottom: 4,
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
    container: {flex: 1, backgroundColor: '#FFF'},
    wrapper: {backgroundColor: '#FFF', flex: 1, marginTop: 10},
    dropDownWrapper: {paddingHorizontal: 25, paddingVertical: 10, zIndex: 100},
    listItemContainer: {
      height: 30,
      backgroundColor: '#fafafb',
    },
    dropDownContainer: {
      borderWidth: 0,
    },
    locationWrapper: {
      position: 'absolute',
      bottom: 0,
      flexDirection: 'row',
      borderRadius: 9,
      backgroundColor: 'rgba(0, 0, 0, 0.3)',
      padding: 2,
      paddingHorizontal: 5,
      margin: 8,
    },
    dropDown2: {
      backgroundColor: '#fafafb',
      borderWidth: 0,
      height: 34,
    },
    dropDownContainer2: {
      borderWidth: 0,
    },
    arrowIcon: {
      width: 18,
      height: 18,
    },
  });
}
