import MypageApiController from '@/services/apis/MypageApiController';
import {STATUSBAR_HEIGHT} from '@/utils/commonParam';
import React from 'react';
import {
  Alert,
  Image,
  ScrollView,
  StyleSheet,
  Text,
  TextInput,
  View,
} from 'react-native';
import {Button} from 'react-native-elements';
import {TouchableOpacity} from 'react-native-gesture-handler';
import {launchImageLibrary} from 'react-native-image-picker';
import {AirbnbRating} from 'react-native-ratings';
import Icon from 'react-native-vector-icons/Ionicons';
import {FileForm} from './editMyProfile';

interface ReviewWriteViewStates {
  text: string;
  uploadUri: string;
  asset: any;
  score: number;
}

export default class ReviewWriteView extends React.Component<
  {},
  ReviewWriteViewStates
> {
  constructor(props: any) {
    super(props);
    this.state = {text: '', uploadUri: '', asset: null, score: 3};
  }

  private checkForm(requestData: any) {
    if (requestData.score <= 0 || requestData.score > 5) {
      Alert.alert('별점은 1점 ~ 5점까지 줄 수 있습니다.');
      return false;
    }

    if (requestData.text.length < 10) {
      Alert.alert('후기는 최소 10자 이상 입력해주세요.');
      return false;
    }

    return true;
  }

  private async postReview(
    lectureId: number,
    scheduleId: number,
    score: number,
    text: string,
    asset: any,
  ) {
    const requestData = {
      lectureId,
      scheduleId,
      score,
      text,
    };

    if (!this.checkForm(requestData)) {
      return;
    }

    const fileData: FileForm = {
      uri: asset.uri,
      type: asset.type ? asset.type : 'image/jpg',
      name: asset.fileName ? asset.fileName : 'image.jpg',
    };

    const multiPart = new FormData();
    multiPart.append('file', fileData);
    multiPart.append('request', {
      string: JSON.stringify(requestData),
      name: 'test1.json',
      type: 'application/json',
    });

    console.log(multiPart);
    this.props.navigation.goBack();

    try {
      await MypageApiController.postReview(multiPart);
    } catch (e) {}
  }

  render() {
    return (
      <View style={styles.container}>
        <View
          style={{
            marginTop: STATUSBAR_HEIGHT,
            height: 60,
            width: '100%',
            flexDirection: 'row',
            justifyContent: 'flex-start',
            alignItems: 'center',
          }}>
          <Button
            icon={<Icon name="chevron-back" size={24} color="#1d1d1f" />}
            buttonStyle={{backgroundColor: 'white'}}
            onPress={() => this.props.navigation.goBack()}
          />
          <Text
            style={{
              fontFamily: 'NotoSansCJKkr-Bold',
              fontSize: 20,
              color: '#1d1d1f',
            }}>
            리뷰 작성하기
          </Text>
        </View>
        <ScrollView style={{marginHorizontal: 20, marginTop: 10}}>
          <View
            style={{
              height: 100,
              justifyContent: 'center',
              alignItems: 'center',
            }}>
            <Text
              style={{
                // width: 93,
                // height: 20,
                fontFamily: 'NotoSansCJKkr-Regular',
                fontSize: 20,
                color: '#1d1d1f',
              }}>
              {this.props.route.params.classInfo.lectureTitle}
            </Text>
          </View>
          <View
            style={{
              height: 100,
              justifyContent: 'center',
              alignItems: 'center',
            }}>
            <AirbnbRating
              showRating={false}
              onFinishRating={(nn: number) => {
                this.setState({score: nn});
              }}
              defaultRating={this.state.score}
            />
          </View>
          <View style={{flex: 1}}>
            <Text
              style={{
                fontSize: 16,
                lineHeight: 18,
                fontFamily: 'NotoSansCJKkr-Bold',
                marginBottom: 10,
              }}>
              리뷰 내용
            </Text>
            <View
              style={{
                // marginHorizontal: 20,
                backgroundColor: 'rgba(245, 245, 247, 0.6)',
                height: 184,
                borderRadius: 6,
                flexDirection: 'row',
                justifyContent: 'space-between',
                alignItems: 'flex-start',
                padding: 10,
              }}>
              <TextInput
                style={{
                  // marginHorizontal: 13,
                  marginTop: 0,
                  numberOfLines: 5,
                }}
                multiline
                onChangeText={text => this.setState({text: text})}
                value={this.state.text}
                placeholder={'리뷰를 입력해주세요'}
              />
            </View>
          </View>

          <View style={{flex: 1, marginTop: 20}}>
            <View
              style={{flexDirection: 'row', justifyContent: 'space-between'}}>
              <View style={{flexDirection: 'row'}}>
                <Text
                  style={{
                    fontSize: 16,
                    lineHeight: 18,
                    fontFamily: 'NotoSansCJKkr-Bold',
                    marginBottom: 10,
                  }}>
                  사진
                </Text>
                <Text
                  style={{
                    fontFamily: 'NotoSansCJKkr-Regular',
                    fontSize: 14,
                    fontWeight: 'normal',
                    fontStyle: 'normal',
                    lineHeight: 20,
                    letterSpacing: 0,
                    color: '#d2d2d2',
                    marginLeft: 10,
                  }}>
                  사진으로 생생한 경험을 보여주세요
                </Text>
              </View>
              <TouchableOpacity
                onPress={() => {
                  launchImageLibrary(
                    {mediaType: 'photo', quality: 0.5},
                    ({assets}) => {
                      if (assets && assets[0].uri) {
                        console.log('assets: ', assets);
                        this.setState({
                          uploadUri: assets[0].uri,
                          asset: assets[0],
                        });
                      }
                    },
                  );
                }}>
                <Text
                  style={{
                    fontFamily: 'NotoSansCJKkr-Bold',
                    fontSize: 14,
                    fontWeight: 'bold',
                    fontStyle: 'normal',
                    letterSpacing: 0,
                    textAlign: 'right',
                    color: '#4f6cff',
                  }}>
                  업로드
                </Text>
              </TouchableOpacity>
            </View>
            <View
              style={{
                // marginHorizontal: 20,
                backgroundColor: 'rgba(245, 245, 247, 0.6)',
                height: 184,
                borderRadius: 6,
                flexDirection: 'row',
                justifyContent: 'space-between',
                alignItems: 'flex-start',
              }}>
              <Image
                style={{height: 184, flex: 1, borderRadius: 6}}
                source={{
                  uri: this.state.uploadUri,
                }}
              />
            </View>
          </View>
          <TouchableOpacity
            style={{
              flex: 1,
              height: 44,
              borderRadius: 6,
              backgroundColor: '#4f6cff',
              marginTop: 20,
              justifyContent: 'center',
              alignItems: 'center',
            }}
            onPress={() => {
              this.postReview(
                this.props.route.params.classInfo.lectureId,
                this.props.route.params.classInfo.scheduleId,
                this.state.score,
                this.state.text,
                this.state.asset,
              );
            }}>
            <Text
              style={{
                fontFamily: 'NotoSansCJKkr-Bold',
                fontSize: 16,
                fontWeight: 'bold',
                fontStyle: 'normal',
                lineHeight: 20,
                letterSpacing: 0,
                textAlign: 'center',
                color: '#ffffff',
              }}>
              작성 완료
            </Text>
          </TouchableOpacity>
        </ScrollView>
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
    paddingHorizontal: 10,
  },
  listContainer: {
    marginHorizontal: 20,
    marginTop: 20,
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
    // marginHorizontal: 10,
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
