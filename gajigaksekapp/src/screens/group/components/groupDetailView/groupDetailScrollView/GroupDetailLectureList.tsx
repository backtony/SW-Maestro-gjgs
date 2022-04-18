import Zone from '@/utils/Zone';
import React from 'react';
import {
  FlatList,
  Image,
  ImageSourcePropType,
  Text,
  TouchableOpacity,
  View,
} from 'react-native';
import Icon from 'react-native-vector-icons/Ionicons';
import {Button} from 'react-native-elements';
import DropDownPicker from 'react-native-dropdown-picker';
import {StyleSheet} from 'react-native';
import LectureApiController from '@/services/apis/LectureApiController';
import FavoriteSelectModal from '@/screens/modal/FavoriteSelectModal';

interface GroupDetailLectureListProps {
  navigation: any;
  title: string;
  imageSource: ImageSourcePropType;
  categoryIdList: number[];
}

interface GroupDetailLectureListStates {
  dropDowns: number[];
  data: any;
  favoriteModalVisible: boolean;
  heartClickedLecture: any;
}

export default class GroupDetailLectureList extends React.Component<
  GroupDetailLectureListProps,
  GroupDetailLectureListStates
> {
  constructor(props: any) {
    super(props);
    this.state = {
      dropDowns: [],
      data: [],
      favoriteModalVisible: false,
      heartClickedLecture: null,
    };
    this.getRecommendLectureList(props.categoryIdList);
  }

  private async getRecommendLectureList(categoryIdList: number[]) {
    try {
      const res = await LectureApiController.getRecommendLectureList(
        categoryIdList,
      );

      console.log('kirin: ', res);

      this.setState({
        data: res.data.content,
        dropDowns: [],
      });
    } catch (e) {
      console.error(e);
    }
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

  render() {
    return (
      <View>
        <View style={this.styles.container}>
          <Image style={this.styles.image} source={this.props.imageSource} />
          <Text style={this.styles.text}>{this.props.title}</Text>
        </View>
        <FlatList
          showsVerticalScrollIndicator={false}
          showsHorizontalScrollIndicator={false}
          data={this.state.data}
          horizontal
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
                    this.props.navigation.navigate('lectureDash', {
                      lectureId: item.lectureId,
                    });
                  }}>
                  <View style={this.styles.imageContainer}>
                    <Image
                      style={this.styles.cardImage}
                      source={{uri: item.imageUrl}}
                    />
                    <View style={this.styles.locationContainer}>
                      <Icon name="location-sharp" size={12} color="#FFF" />

                      <Text style={this.styles.zone}>
                        {Zone.getZone(item.zoneId)[1]}
                      </Text>
                    </View>
                    <View style={this.styles.heartWrapper}>
                      <Button
                        icon={
                          item.myFavorite ? (
                            <Icon name="heart" size={30} color="#ff4f4f" />
                          ) : (
                            <Icon name="heart-outline" size={30} color="#FFF" />
                          )
                        }
                        buttonStyle={this.styles.button}
                        onPress={() => {
                          this.setState({heartClickedLecture: item});
                          this.setState({favoriteModalVisible: true});
                        }}
                      />
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
                      label: `${item.priceOne}원`,
                      value: 'one',
                      icon: () => <Text style={{fontSize: 12}}>1인</Text>,
                    },
                    {
                      label: `${item.priceTwo}원`,
                      value: 'two',
                      icon: () => <Text style={{fontSize: 12}}>2인</Text>,
                    },
                    {
                      label: `${item.priceThree}원`,
                      value: 'three',
                      icon: () => <Text style={{fontSize: 12}}>3인</Text>,
                    },
                    {
                      label: `${item.priceFour}원`,
                      value: 'four',
                      icon: () => <Text style={{fontSize: 12}}>4인 이상</Text>,
                    },
                  ]}
                  setOpen={() => {
                    this.toggleDropDown(item.lectureId);
                  }}
                  setValue={() => {}}
                  setItems={() => {}}
                  showTickIcon={false}
                  dropDownDirection="TOP"
                  style={this.styles.dropDownPicker}
                  listItemContainerStyle={this.styles.listItemContainer}
                  listItemLabelStyle={this.styles.listItemLabel}
                  dropDownContainerStyle={this.styles.dropDownContainer}
                  arrowIconStyle={this.styles.arrowIcon}
                  textStyle={this.styles.dropDownText}
                />
              </View>
            );
          }}
        />
        <View>
          <FavoriteSelectModal
            modalVisible={this.state.favoriteModalVisible}
            lecture={
              this.state.heartClickedLecture
                ? this.state.heartClickedLecture
                : null
            }
            setModalVisible={(visible: boolean) => {
              this.setState({favoriteModalVisible: visible});
            }}
            setFavorite={() => {
              this.getRecommendLectureList(this.props.categoryIdList);
            }}
          />
        </View>
      </View>
    );
  }

  private styles = StyleSheet.create({
    separator: {
      marginTop: 10,
    },
    card: {
      backgroundColor: 'white',
      width: 152,
      marginRight: 10,
      marginBottom: 15,
    },
    imageContainer: {
      borderRadius: 10,
    },
    cardImage: {
      flex: 1,
      height: 122,
      borderRadius: 9,
    },
    zone: {
      fontSize: 10,
      color: '#FFF',
    },
    cardContent: {
      paddingTop: 7,
      paddingLeft: 4,
      justifyContent: 'space-between',
    },
    title: {
      fontFamily: 'NotoSansCJKkr-Bold',
      fontSize: 14,
      lineHeight: 16,
      color: '#070707',
      marginBottom: 4,
    },
    container: {
      flexDirection: 'row',
      marginTop: 40,
      alignItems: 'center',
      marginBottom: 20,
    },
    image: {width: 16, height: 16},
    text: {
      fontSize: 16,
      lineHeight: 18,
      fontFamily: 'NotoSansCJKkr-Bold',
      marginTop: 7,
      marginLeft: 6,
    },
    locationContainer: {
      position: 'absolute',
      bottom: 0,
      flexDirection: 'row',
      borderRadius: 9,
      backgroundColor: 'rgba(0, 0, 0, 0.3)',
      padding: 2,
      paddingHorizontal: 5,
      margin: 8,
    },
    button: {
      backgroundColor: 'transparent',
    },
    dropDownPicker: {
      backgroundColor: '#fafafb',
      borderWidth: 0,
      height: 34,
    },
    dropDownContainer: {
      borderWidth: 0,
    },
    arrowIcon: {
      width: 18,
      height: 18,
    },
    listItemContainer: {height: 28},
    listItemLabel: {color: '#4a4a4c', fontSize: 12},
    dropDownText: {fontSize: 14, fontWeight: 'bold'},
    heartWrapper: {position: 'absolute', right: 0},
  });
}
