import React, {Component} from 'react';
import {
  StyleSheet,
  Text,
  View,
  TouchableOpacity,
  Alert,
  TextInput,
} from 'react-native';
import {STATUSBAR_HEIGHT} from '../../../utils/commonParam';
import {Button} from 'react-native-elements';
import Icon from 'react-native-vector-icons/Ionicons';
import MypageApiController from '../../../services/apis/MypageApiController';
import QuestionApiController from '../../../services/apis/QuestionApiController';

export default class LectureQuestionWriteView extends Component<
  {},
  {text: string}
> {
  constructor(props: any) {
    super(props);
    this.state = {text: ''};
    this.getBulletin();
  }
  private async getBulletin() {
    try {
      const res = await MypageApiController.getBulletin({});
      console.log('res: ', res.data.myBulletinDtoList);

      res.data.myBulletinDtoList = res.data.myBulletinDtoList.map(item => {
        item.thumbnailImageFileUrl =
          'https://via.placeholder.com/400x200/FFB6C1/000000';
        return item;
      });

      this.setState({data: res?.data.myBulletinDtoList});
    } catch (e) {
      console.error(e);
    }
  }

  private async postQuestion(lectureId: number) {
    try {
      await QuestionApiController.postQuestion({
        lectureId: lectureId,
        questionForm: this.state.text,
      });
      Alert.alert('문의글 작성이 완료되었습니다.');
      this.props.route.params.refreshParent();
      this.props.navigation.goBack();
    } catch (e) {
      console.error(e);
    }
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
          <Text style={this.styles.headerTitle}>문의 작성</Text>
        </View>
        <View style={{flex: 1, paddingHorizontal: 20}}>
          <Text style={this.styles.text}>문의 내용</Text>
          <View style={this.styles.inputWrapper}>
            <TextInput
              style={this.styles.input}
              multiline
              onChangeText={text => this.setState({text: text})}
              value={this.state.text}
              placeholder={'본문을 입력해주세요'}
            />
          </View>
        </View>

        <TouchableOpacity
          style={this.styles.button}
          onPress={() => {
            this.postQuestion(this.props.route.params.lectureId);
          }}>
          <Icon name="paper-plane" size={20} color="#FFF" />
          <Text style={this.styles.buttonText}>등록하기</Text>
        </TouchableOpacity>
      </View>
    );
  }
  private styles = StyleSheet.create({
    buttonText: {
      fontSize: 16,
      fontWeight: 'bold',
      color: '#FFF',
      marginLeft: 4,
    },
    button: {
      padding: 12,
      backgroundColor: '#4f6cff',
      borderRadius: 6,
      flexDirection: 'row',
      justifyContent: 'center',
      alignItems: 'center',
      marginTop: 10,
      marginBottom: 20,
      marginHorizontal: 20,
    },
    input: {
      marginTop: 0,
    },
    inputWrapper: {
      backgroundColor: 'rgba(245, 245, 247, 0.6)',
      height: 184,
      borderRadius: 6,
      flexDirection: 'row',
      justifyContent: 'space-between',
      alignItems: 'flex-start',
      padding: 10,
    },
    text: {
      fontSize: 16,
      lineHeight: 18,
      fontFamily: 'NotoSansCJKkr-Bold',
      marginTop: 40,
      marginBottom: 10,
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
      fontFamily: 'NotoSansCJKkr-Bold',
      fontSize: 20,
      color: '#1d1d1f',
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
