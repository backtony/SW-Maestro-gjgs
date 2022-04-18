import React from 'react';
import {StyleSheet, Image, Text, View, ScrollView, Modal} from 'react-native';
import Zone from '../../utils/Zone';
import {Button} from 'react-native-elements';
import Icon from 'react-native-vector-icons/Ionicons';
import DropDownPicker from 'react-native-dropdown-picker';
import FavoriteSelectModalGroupBlock from './FavoriteSelectModalGroupBlock';
import FavoriteApiController from '../../services/apis/FavoriteApiController';

export default class FavoriteSelectModal extends React.Component<
  {
    modalVisible: boolean;
    lecture: any;
    setModalVisible: (visible: boolean) => void;
    setFavorite: (sub: string) => void;
  },
  {
    selectedMainZone: string;
    selectedSubZone: string;
    dropDown: boolean;
    selectedGroup: number[];

    teamList: any;
    selectedPrice: string;
  }
> {
  constructor(props: any) {
    super(props);
    this.state = {
      selectedMainZone: 'ss',
      selectedSubZone: 'dd',
      dropDown: false,
      selectedGroup: [],
      selectedPrice: 'one',
    };
    console.log('props: ', props);
  }

  private beforeLecture: any;

  componentDidUpdate() {
    if (this.props.lecture && this.beforeLecture !== this.props.lecture) {
      this.beforeLecture = this.props.lecture;
      this.getTeamList();
    }
  }

  private async getTeamList() {
    try {
      const res = await FavoriteApiController.getTeamList({
        lectureId: this.props.lecture.lectureId,
      });
      this.setState({
        teamList: res.data.myTeamAndIsIncludeFavoriteLectureDtoList,
      });
    } catch (e) {
      console.error(e);
    }
  }

  private async postFavoriteIndividual(lectureId: number) {
    console.log('lectureId: ', lectureId);
    try {
      await FavoriteApiController.postFavoriteIndividual({
        lectureId,
      });
      this.props.setFavorite('');
    } catch (e) {
      console.error(e);
    }
  }

  private async deleteFavoriteIndividual() {
    try {
      await FavoriteApiController.deleteFavoriteIndividual({
        lectureId: this.props.lecture.lectureId,
      });
      this.props.setFavorite('');
    } catch (e) {
      console.error(e);
    }
  }

  private async postFavoriteTeam(teamId: number) {
    try {
      await FavoriteApiController.postFavoriteTeam({
        lectureId: this.props.lecture.lectureId,
        teamId: teamId,
      });
      this.getTeamList();
    } catch (e) {
      console.error(e);
    }
  }

  private async deleteFavoriteTeam(teamId: number) {
    try {
      await FavoriteApiController.deleteFavoriteTeam({
        lectureId: this.props.lecture.lectureId,
        teamId: teamId,
      });
      this.getTeamList();
    } catch (e) {
      console.error(e);
    }
  }

  render() {
    return (
      <Modal
        animationType="slide"
        transparent={true}
        visible={this.props.modalVisible}
        onRequestClose={() => {
          this.props.setModalVisible(false);
        }}>
        <View style={{flex: 1}} />
        <View style={this.modalStyles.centeredView}>
          <View style={this.modalStyles.modalView}>
            <View style={this.modalStyles.wrapper1}>
              <Text style={{fontSize: 20, fontFamily: 'NotoSansCJKkr-Bold'}}>
                찜 하기
              </Text>
              <Button
                icon={<Icon name="close" size={24} color="#1d1d1f" />}
                buttonStyle={{backgroundColor: 'white'}}
                onPress={() => {
                  this.props.setModalVisible(false);
                }}
              />
            </View>

            <View style={{width: '100%'}}>
              <View style={this.modalStyles.innerModalConatiner}>
                {this.props.lecture && (
                  <View style={styles.card}>
                    <View style={styles.imageContainer}>
                      <Image
                        style={styles.cardImage}
                        source={{
                          uri: this.props.lecture.imageUrl,
                        }}
                      />
                    </View>
                    <View style={{backgroundColor: 'transparent', width: 200}}>
                      <View style={styles.cardContent}>
                        <Text style={styles.title}>
                          {this.props.lecture.title}
                        </Text>
                      </View>

                      <View style={this.modalStyles.wrapper2}>
                        <Icon name="location-sharp" size={12} color="#FFF" />
                        <Text style={styles.zone}>
                          {Zone.getZone(this.props.lecture.zoneId)[1]}
                        </Text>
                      </View>

                      <DropDownPicker
                        open={this.state.dropDown}
                        value={this.state.selectedPrice}
                        items={[
                          {
                            label: `${this.props.lecture.priceOne}원`,
                            value: 'one',
                            icon: () => <Text style={{fontSize: 12}}>1인</Text>,
                          },
                          {
                            label: `${this.props.lecture.priceTwo}원`,
                            value: 'two',
                            icon: () => <Text style={{fontSize: 12}}>2인</Text>,
                          },
                          {
                            label: `${this.props.lecture.priceThree}원`,
                            value: 'three',
                            icon: () => <Text style={{fontSize: 12}}>3인</Text>,
                          },
                          {
                            label: `${this.props.lecture.priceFour}원`,
                            value: 'four',
                            icon: () => (
                              <Text style={{fontSize: 12}}>4인 이상</Text>
                            ),
                          },
                        ]}
                        setOpen={() => {
                          this.setState({dropDown: !this.state.dropDown});
                        }}
                        setValue={callback => {
                          this.setState(state => ({
                            selectedPrice: callback(state.selectedPrice),
                          }));
                        }}
                        setItems={() => {}}
                        showTickIcon={false}
                        dropDownDirection="TOP"
                        style={this.modalStyles.dropDown}
                        listItemContainerStyle={{height: 28}}
                        listItemLabelStyle={{color: '#4a4a4c', fontSize: 12}} //dd
                        dropDownContainerStyle={{
                          borderWidth: 0,
                        }}
                        arrowIconStyle={{
                          width: 18,
                          height: 18,
                        }}
                        textStyle={{fontSize: 14, fontWeight: 'bold'}}
                      />
                    </View>
                  </View>
                )}

                <ScrollView
                  style={{height: '100%'}}
                  showsVerticalScrollIndicator={false}
                  showsHorizontalScrollIndicator={false}>
                  <FavoriteSelectModalGroupBlock
                    group={{
                      title: '나의 찜 목록',
                      id: -1,
                    }}
                    checked={
                      this.props.lecture && this.props.lecture.myFavorite
                    }
                    onChange={(id: number) => {
                      if (!this.props.lecture.myFavorite) {
                        this.postFavoriteIndividual(
                          this.props.lecture.lectureId,
                        );
                      }

                      if (this.props.lecture.myFavorite) {
                        this.deleteFavoriteIndividual();
                      }
                    }}
                  />
                  {this.state.teamList &&
                    this.state.teamList.map(team => (
                      <FavoriteSelectModalGroupBlock
                        group={{
                          title: team.teamName,
                          id: team.teamId,
                        }}
                        checked={team.include}
                        onChange={(id: number) => {
                          // this.onChangeGroup(id);
                          if (!team.include) {
                            this.postFavoriteTeam(id);
                          }

                          if (team.include) {
                            this.deleteFavoriteTeam(id);
                          }
                        }}
                      />
                    ))}
                  <View style={{height: 400}} />
                </ScrollView>
              </View>
            </View>
          </View>
        </View>
      </Modal>
    );
  }

  private modalStyles = StyleSheet.create({
    wrapper1: {
      flexDirection: 'row',
      alignItems: 'center',
      justifyContent: 'space-between',
      width: '100%',
      height: 60,
      backgroundColor: '#fff',
      borderBottomWidth: 1,
      borderColor: '#f5f5f7',
    },
    wrapper2: {
      bottom: 0,
      flexDirection: 'row',
      borderRadius: 9,
      backgroundColor: 'rgba(0, 0, 0, 0.3)',
      padding: 2,
      paddingHorizontal: 5,
      marginVertical: 5,
      alignSelf: 'flex-start',
    },
    dropDown: {
      backgroundColor: '#fafafb',
      borderWidth: 0,
      height: 34,
    },
    centeredView: {
      flex: 13,
      justifyContent: 'center',
      alignItems: 'center',
      width: '100%',
    },
    modalView: {
      // margin: 20,

      backgroundColor: 'white',
      borderRadius: 20,
      padding: 20,
      // alignItems: 'center',
      shadowColor: '#000',
      shadowOffset: {
        width: 0,
        height: 2,
      },
      shadowOpacity: 0.25,
      shadowRadius: 4,
      elevation: 5,
      width: '100%',
      flex: 2,
    },
    button: {
      borderRadius: 20,
      padding: 10,
      elevation: 2,
    },
    buttonOpen: {
      backgroundColor: '#F194FF',
    },
    buttonClose: {
      backgroundColor: '#2196F3',
    },
    textStyle: {
      color: 'white',
      fontWeight: 'bold',
      textAlign: 'center',
    },
    modalText: {
      marginBottom: 15,
      textAlign: 'center',
    },
    innerModalConatiner: {
      //   flexDirection: 'row',
      marginTop: 20,
      // backgroundColor: 'grey',
    },
    scrollView: {
      width: '50%',
    },
    buttonText: {
      fontSize: 12,
      fontFamily: 'NotoSansCJKkr-Bold',
      lineHeight: 14,
      color: '#fff',
    },
    button2: {
      height: 40,
      borderRadius: 8,
      justifyContent: 'center',
      paddingLeft: 12,
      marginBottom: 10,
    },
    buttonOn: {backgroundColor: '#4f6cff'},
    buttonOff: {backgroundColor: '#fafafb'},
  });
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#FFF',
  },
  list: {
    // paddingHorizontal: 10,
  },
  listContainer: {
    // marginHorizontal: 20,
    // marginTop: 20,
    // backgroundColor: 'red',
  },
  separator: {
    marginTop: 10,
  },
  /******** card **************/
  card: {
    // marginVertical: 8,
    // backgroundColor: 'blue',
    // flexBasis: '45%',
    // marginRight: 10,
    marginBottom: 15,
    flexDirection: 'row',
    // width: '100%',
  },
  cardContent: {
    // paddingTop: 7,
    paddingLeft: 4,
    // justifyContent: 'space-between',
    // backgroundColor: 'skyblue',
  },
  cardImage: {
    flex: 1,
    height: 122,
    width: 122,
    borderRadius: 9,
  },
  imageContainer: {
    // shadowColor: '#000',
    // shadowOffset: {
    //   width: 0,
    //   height: 4,
    // },
    // shadowOpacity: 0.32,
    // shadowRadius: 5.46,

    // elevation: 9,
    borderRadius: 10,
    width: 120,
    height: 96,
    marginRight: 16,
  },
  /******** card components **************/
  zone: {
    fontSize: 10,
    // flex: 1,
    color: '#FFF',

    // bottom: 0,
  },
  title: {
    // width: 93,
    // height: 20,
    fontFamily: 'NotoSansCJKkr-Bold',
    fontSize: 14,
    lineHeight: 16,
    color: '#070707',
  },
  age: {
    fontFamily: 'NotoSansCJKkr-Regular',
    fontSize: 12,
    lineHeight: 14,
    color: '#4a4a4c',
    marginBottom: 4,
  },
  time: {
    fontSize: 18,
    flex: 1,
    color: '#778899',
  },
  people: {
    fontFamily: 'NotoSansCJKkr-Regular',
    fontSize: 14,
    lineHeight: 16,
    color: '#4a4a4c',
  },
  peopleBold: {
    fontFamily: 'NotoSansCJKkr-Bold',
    fontSize: 14,
    lineHeight: 16,
    color: '#4f6cff',
    marginLeft: 5,
  },
  count: {
    fontSize: 18,
    flex: 1,
    color: '#B0C4DE',
  },
});
