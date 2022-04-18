import Zone from '@/utils/Zone';
import React from 'react';
import {
  FlatList,
  Image,
  SafeAreaView,
  StyleSheet,
  Text,
  TouchableOpacity,
  View,
} from 'react-native';
import DropDownPicker from 'react-native-dropdown-picker';
import {Button} from 'react-native-elements';
import Icon from 'react-native-vector-icons/Ionicons';

interface LectureDetailViewRecommendListProps {
  navigation: any;
  data: any;
  modalVisible: boolean;
  toggleFavorite: (lectureId: number) => void;
  setModalVisible: (modalVisible: boolean) => void;
}

interface LectureDetailViewRecommendListStates {
  dropDowns: number[];
}

export default class LectureDetailViewRecommendList extends React.Component<
  LectureDetailViewRecommendListProps,
  LectureDetailViewRecommendListStates
> {
  constructor(props: any) {
    super(props);
    this.state = {dropDowns: []};
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
        <View
          style={{
            flexDirection: 'row',
            marginTop: 40,
            alignItems: 'center',
            marginBottom: 20,
            marginHorizontal: 20,
          }}>
          <Image
            style={{width: 16, height: 16}}
            source={require('gajigaksekapp/src/asset/iconImage/goodjob.png')}
          />
          <Text
            style={{
              fontSize: 16,
              lineHeight: 18,
              fontFamily: 'NotoSansCJKkr-Bold',
              marginTop: 7,
              marginLeft: 6,
            }}>
            가지각색 추천 클래스
          </Text>
        </View>
        <SafeAreaView>
          <FlatList
            style={styles.list}
            contentContainerStyle={styles.listContainer}
            ListHeaderComponent={() => <View />}
            ListFooterComponent={() => <View />}
            ListEmptyComponent={() => <View />}
            // columnWrapperStyle={{justifyContent: 'space-between'}}
            showsVerticalScrollIndicator={false}
            showsHorizontalScrollIndicator={false}
            data={this.props.data}
            horizontal
            // numColumns={2}
            keyExtractor={item => {
              return item.id;
            }}
            ItemSeparatorComponent={() => {
              return <View style={styles.separator} />;
            }}
            renderItem={post => {
              const item = post.item;
              return (
                <View style={styles.card}>
                  <TouchableOpacity
                    onPress={() => {
                      this.props.navigation.push('lectureDash', {
                        lectureId: item.lectureId,
                      });
                    }}>
                    <View style={styles.imageContainer}>
                      <Image
                        style={styles.cardImage}
                        source={{uri: item.image}}
                      />
                      <View
                        style={{
                          position: 'absolute',
                          bottom: 0,
                          flexDirection: 'row',
                          borderRadius: 9,
                          backgroundColor: 'rgba(0, 0, 0, 0.3)',
                          padding: 2,
                          paddingHorizontal: 5,
                          margin: 8,
                        }}>
                        <Icon name="location-sharp" size={12} color="#FFF" />

                        <Text style={styles.zone}>
                          {Zone.getZone(item.zoneId)[1]}
                        </Text>
                      </View>
                      <View style={{position: 'absolute', right: 0}}>
                        <Button
                          icon={
                            item.myFaovirte ? (
                              <Icon name="heart" size={30} color="#ff4f4f" />
                            ) : (
                              <Icon
                                name="heart-outline"
                                size={30}
                                color="#FFF"
                              />
                            )
                          }
                          buttonStyle={{
                            backgroundColor: 'transparent',
                          }}
                          onPress={() => {
                            this.props.toggleFavorite(item.lectureId);
                            this.props.setModalVisible(
                              !this.props.modalVisible,
                            );
                          }}
                        />
                      </View>
                    </View>

                    <View style={styles.cardContent}>
                      <Text style={styles.title}>{item.title}</Text>
                    </View>
                  </TouchableOpacity>
                  <DropDownPicker
                    open={!!this.state.dropDowns.includes(item.lectureId)}
                    value={'one'}
                    items={[
                      {
                        label: `${item.priceList.one}원`,
                        value: 'one',
                        icon: () => <Text style={{fontSize: 12}}>1인</Text>,
                      },
                      {
                        label: `${item.priceList.two}원`,
                        value: 'two',
                        icon: () => <Text style={{fontSize: 12}}>2인</Text>,
                      },
                      {
                        label: `${item.priceList.three}원`,
                        value: 'three',
                        icon: () => <Text style={{fontSize: 12}}>3인</Text>,
                      },
                      {
                        label: `${item.priceList.four}원`,
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
                    style={{
                      backgroundColor: '#fafafb',
                      borderWidth: 0,
                      height: 34,
                    }}
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
          />
        </SafeAreaView>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#FFF',
  },
  list: {
    marginHorizontal: 20,
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
    backgroundColor: 'white',
    width: 152,
    // flexBasis: '45%',
    marginRight: 10,
    marginBottom: 15,
  },
  cardContent: {
    paddingTop: 7,
    paddingLeft: 4,
    justifyContent: 'space-between',
    // backgroundColor: 'skyblue',
  },
  cardImage: {
    flex: 1,
    height: 122,
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
    marginBottom: 4,
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
