import React from 'react';
import {StyleSheet, TouchableOpacity, Image, FlatList} from 'react-native';
import {View, Text} from 'react-native';
import Icon from 'react-native-vector-icons/Ionicons';
import Zone from '../../utils/Zone';
import DropDownPicker from 'react-native-dropdown-picker';
import FavoriteApiController from '../../services/apis/FavoriteApiController';

interface BulletinLectureSelectLectureTabProps {
  onSelectLecture: (lectureId: number) => void;
  selectedLectureId: number;
}

interface BulletinLectureSelectLectureTabStates {
  data: any;

  dropDowns: number[];

  refreshing: boolean;
  heartClickedLecture: any;
}

export default class BulletinLectureSelectLectureTab extends React.Component<
  BulletinLectureSelectLectureTabProps,
  BulletinLectureSelectLectureTabStates
> {
  constructor(props: any) {
    super(props);
    this.state = {
      data: [],
      dropDowns: [],
      refreshing: false,
    };
  }

  componentDidMount() {
    this.getFavoriteLecture();
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

  private async getFavoriteLecture() {
    try {
      const res = await FavoriteApiController.getLectureIndividual({});

      this.setState({data: res.data.lectureMemberDtoList});
    } catch (e) {
      console.error(e);
    }
  }

  render() {
    return (
      <View style={this.styles.container}>
        <View style={this.styles.listWrapper}>
          <FlatList
            style={this.styles.list}
            contentContainerStyle={this.styles.listContainer}
            data={this.state.data}
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
                  {this.props.selectedLectureId === item.lectureId && (
                    <View style={this.styles.clickMarker}>
                      <Icon name="checkmark-circle" size={50} color="#4f6cff" />
                    </View>
                  )}
                  <TouchableOpacity
                    onPress={() => {
                      this.props.onSelectLecture(item.lectureId);
                    }}>
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
                    style={this.styles.dropDown}
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
              );
            }}
            refreshing={this.state.refreshing}
            onRefresh={() => this.getFavoriteLecture()}
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
    clickMarker: {
      position: 'absolute',
      top: 0,
      backgroundColor: 'rgba(52, 52, 52, 0.2)',
      zIndex: 100,
      width: '100%',
      height: '100%',
      justifyContent: 'center',
      alignItems: 'center',
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
    dropDown: {
      backgroundColor: '#fafafb',
      borderWidth: 0,
      height: 34,
    },
    container: {flex: 1, backgroundColor: '#FFF'},
    listWrapper: {backgroundColor: '#FFF', flex: 1, marginTop: 10},
  });
}
