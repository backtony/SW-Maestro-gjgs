import React from 'react';
import {StyleSheet, TouchableOpacity, Image, FlatList} from 'react-native';
import {View, Text} from 'react-native';
import Icon from 'react-native-vector-icons/Ionicons';
import Zone from '../../utils/Zone';
import DropDownPicker from 'react-native-dropdown-picker';
import GroupApiController from '../../services/apis/GroupApiController';
import UserDC from '../../services/login/UserDC';
import FavoriteApiController from '../../services/apis/FavoriteApiController';
import BulletinLectureSelectGroupTabDropDown from './components/BulletinLectureSelectGroupTabDropDown';

interface BulletinLectureSelectGroupTabProps {
  onSelectLecture: (lectureId: number) => void;
  selectedLectureId: number;
}

export default class BulletinLectureSelectGroupTab extends React.Component<
  BulletinLectureSelectGroupTabProps,
  {
    data: any;
    dropDowns: number[];
    groupDropDown: boolean;

    selectedGroup: number;
    teamList: any;

    refreshing: boolean;
    teamId: number;
  }
> {
  constructor(props: any) {
    super(props);
    this.state = {
      data: [],
      dropDowns: [],
      groupDropDown: false,
      selectedGroup: 0,
      teamList: [],

      refreshing: false,
      teamId: 0,
    };
    this.getGroupListApi();
  }

  private toggleDropDown = (id: number) => {
    const updateddropDowns = this.state.dropDowns;
    if (updateddropDowns.includes(id)) {
      updateddropDowns.splice(updateddropDowns.indexOf(id), 1);
    } else {
      updateddropDowns.push(id);
    }

    this.setState({dropDowns: updateddropDowns});
  };

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

  private async getTeamLecture(teamId: number) {
    if (teamId === 0) {
      this.setState({data: []});
      return;
    }
    try {
      const res = await FavoriteApiController.getTeamLecture({
        teamId: this.state.selectedGroup,
      });
      this.setState({data: res.data.lectureTeamDtoList, teamId});
    } catch (e) {
      console.error(e);
    }
  }

  render() {
    return (
      <View style={this.styles.container}>
        <View style={this.styles.wrapper}>
          <BulletinLectureSelectGroupTabDropDown
            onChangeGroup={(teamId: number) => this.getTeamLecture(teamId)}
          />
          <FlatList
            style={this.styles.list}
            contentContainerStyle={this.styles.listContainer}
            // columnWrapperStyle={{justifyContent: 'space-between'}}
            data={this.state.data}
            // horizontal
            numColumns={2}
            keyExtractor={item => {
              return item.id;
            }}
            ItemSeparatorComponent={() => {
              return <View style={this.styles.separator} />;
            }}
            renderItem={post => {
              const item = post.item;
              return (
                <View style={this.styles.card}>
                  <TouchableOpacity
                    onPress={() => {
                      this.props.onSelectLecture(item.lectureId);
                    }}
                    style={
                      this.props.selectedLectureId === item.lectureId
                        ? {backgroundColor: 'red'}
                        : {}
                    }>
                    <View style={this.styles.imageContainer}>
                      <Image
                        style={this.styles.cardImage}
                        source={{uri: item.thumbnailImageFileUrl}}
                      />
                      <View style={this.styles.locationWrapper}>
                        <Icon name="location-sharp" size={12} color="#FFF" />

                        <Text style={this.styles.zone}>
                          {Zone.getZone(item.zoneId)[1]}
                        </Text>
                      </View>
                    </View>

                    <View style={this.styles.cardContent}>
                      <Text style={this.styles.title}>{item.title}</Text>
                    </View>
                  </TouchableOpacity>
                  <DropDownPicker
                    open={!!this.state.dropDowns.includes(item.lectureId)}
                    value={'one'}
                    items={[
                      {
                        label: `${item.price.priceOne}원`,
                        value: 'one',
                        icon: () => <Text style={{fontSize: 12}}>1인</Text>,
                      },
                      {
                        label: `${item.price.priceTwo}원`,
                        value: 'two',
                        icon: () => <Text style={{fontSize: 12}}>2인</Text>,
                      },
                      {
                        label: `${item.price.priceThree}원`,
                        value: 'three',
                        icon: () => <Text style={{fontSize: 12}}>3인</Text>,
                      },
                      {
                        label: `${item.price.priceFour}원`,
                        value: 'four',
                        icon: () => (
                          <Text style={{fontSize: 12}}>4인 이상</Text>
                        ),
                      },
                    ]}
                    setOpen={() => {
                      this.toggleDropDown(item.lectureId);
                    }}
                    setValue={() => {}}
                    setItems={() => {}}
                    showTickIcon={false}
                    dropDownDirection="TOP"
                    style={this.styles.dropDown2}
                    listItemContainerStyle={{height: 28}}
                    listItemLabelStyle={{color: '#4a4a4c', fontSize: 12}} //dd
                    dropDownContainerStyle={this.styles.dropDownContainer2}
                    arrowIconStyle={this.styles.arrowIcon}
                    textStyle={{fontSize: 14, fontWeight: 'bold'}}
                  />
                </View>
              );
            }}
            refreshing={this.state.refreshing}
            onRefresh={() => this.getTeamLecture(this.state.teamId)}
          />
        </View>
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
