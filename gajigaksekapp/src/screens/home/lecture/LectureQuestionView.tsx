import React, {Component} from 'react';
import {
  StyleSheet,
  Text,
  View,
  TouchableOpacity,
  Image,
  ScrollView,
} from 'react-native';
import {STATUSBAR_HEIGHT} from '../../../utils/commonParam';
import {Button} from 'react-native-elements';
import Icon from 'react-native-vector-icons/Ionicons';

export default class LectureQuestionView extends Component {
  constructor(props: any) {
    super(props);
    this.state = {data: []};
  }

  private timeLapse(time: number) {
    const minute = Math.floor((Date.now() - Date.parse(time)) / 1000 / 60);

    if (minute < 60) {
      return `${minute}분 전`;
    } else if (minute < 1440) {
      return `${Math.floor(minute / 60)}시간 전`;
    } else if (minute < 44640) {
      return `${Math.floor(minute / 1440)}달 전`;
    } else if (minute < 525600) {
      return `${Math.floor(minute / 44640)}년 전`;
    }

    return minute;
  }

  render() {
    return (
      <View style={styles.container}>
        <View style={this.styles.header}>
          <Button
            icon={<Icon name="chevron-back" size={24} color="#1d1d1f" />}
            buttonStyle={{backgroundColor: 'white'}}
            onPress={() => this.props.navigation.goBack()}
          />
          <Text style={this.styles.headerTitle}>문의하기</Text>
        </View>
        <View style={{paddingHorizontal: 20}}>
          <ScrollView>
            {this.props.route.params.lecture &&
              this.props.route.params.questionList.map(question => {
                return (
                  <TouchableOpacity
                    style={this.styles.button}
                    onPress={() => {
                      this.props.navigation.navigate('lectureQuestionDash', {
                        questionId: question.questionId,
                        bottom: false,
                      });
                    }}>
                    <Image
                      style={this.styles.avatar}
                      source={{
                        uri: question.questionerProfileImageUrl,
                      }}
                    />
                    <View>
                      <View style={{flexDirection: 'row'}}>
                        <Text style={this.styles.text}>
                          {question.questionerNickname}
                        </Text>
                        <Text style={this.styles.text2}>
                          {this.timeLapse(question.questionDate)}
                        </Text>
                      </View>
                      <Text numberOfLines={1} style={{width: '80%'}}>
                        {question.questionText}
                      </Text>
                    </View>
                  </TouchableOpacity>
                );
              })}
          </ScrollView>
        </View>
      </View>
    );
  }
  private styles = StyleSheet.create({
    text2: {
      color: '#8e8e8f',
      fontSize: 12,
      lineHeight: 14,
      fontFamily: 'NotoSansCJKkr-Regular',
      marginTop: 2,
      marginLeft: 5,
    },
    text: {
      color: '#1d1d1f',
      fontSize: 16,
      lineHeight: 18,
      fontFamily: 'NotoSansCJKkr-Bold',
    },
    button: {
      flexDirection: 'row',
      alignItems: 'center',
      paddingVertical: 15,
      borderBottomWidth: 1,
      borderBottomColor: '#f5f5f7',
    },
    header: {
      marginTop: STATUSBAR_HEIGHT,
      height: 60,
      width: '100%',
      flexDirection: 'row',
      justifyContent: 'flex-start',
      alignItems: 'center',
    },
    headerTitle: {
      // width: 93,
      // height: 20,
      fontFamily: 'NotoSansCJKkr-Bold',
      fontSize: 20,
      color: '#1d1d1f',
    },
    flatList: {
      width: '100%',
      padding: 10,
    },
    block: {
      height: 50,
      width: '100%',
      backgroundColor: '#5d5d67',
      marginTop: 10,
      borderRadius: 10,
      justifyContent: 'center',
      alignItems: 'center',
    },
    avatar: {
      width: 48,
      height: 48,
      borderRadius: 48,
      borderWidth: 1,
      borderColor: '#f5f5f7',
      borderStyle: 'solid',
      marginLeft: 8,
      marginRight: 16,
    },
  });
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
