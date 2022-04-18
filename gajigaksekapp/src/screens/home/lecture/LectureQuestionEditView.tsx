import React, {Component} from 'react';
import {
  StyleSheet,
  Text,
  View,
  TouchableOpacity,
  TextInput,
} from 'react-native';
import {STATUSBAR_HEIGHT} from '../../../utils/commonParam';
import {Button} from 'react-native-elements';
import Icon from 'react-native-vector-icons/Ionicons';
import MypageApiController from '../../../services/apis/MypageApiController';
import QuestionApiController from '../../../services/apis/QuestionApiController';

export default class LectureQuestionEditView extends Component<
  {},
  {text: string}
> {
  constructor() {
    super();
    this.state = {text: ''};
    this.getBulletin();
  }

  componentDidMount() {
    this.setState({
      text: this.props.route.params.questionText,
    });
  }

  private async getBulletin() {
    try {
      const res = await MypageApiController.getBulletin({});
      console.log('res: ', res?.data.myBulletinDtoList);

      res.data.myBulletinDtoList = res?.data.myBulletinDtoList.map(item => {
        item.thumbnailImageFileUrl =
          'https://via.placeholder.com/400x200/FFB6C1/000000';
        return item;
      });

      this.setState({data: res?.data.myBulletinDtoList});
    } catch (e) {
      console.error(e);
    }
  }

  private async putQuestionEdit(questionId: number) {
    if (this.state.text.length < 10 || this.state.text.length > 100) {
      alert('10자이상 100자이하로 적어주세요.');
      return;
    }
    try {
      await QuestionApiController.putQuestionEdit({
        questionId,
        mainText: this.state.text,
      });
      alert('문의글 수정이 완료되었습니다.');
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
          <Text style={this.styles.headerTitle}>문의 수정</Text>
        </View>
        <View style={{flex: 1, paddingHorizontal: 20}}>
          <Text style={this.styles.text1}>문의 내용</Text>
          <View style={this.styles.inputWrapper}>
            <TextInput
              style={this.styles.textInput}
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
            this.putQuestionEdit(this.props.route.params.questionId);
          }}>
          <Text style={this.styles.buttonText}>수정하기</Text>
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
    textInput: {
      // marginHorizontal: 13,
      marginTop: 0,
    },
    inputWrapper: {
      // marginHorizontal: 20,
      backgroundColor: 'rgba(245, 245, 247, 0.6)',
      height: 184,
      borderRadius: 6,
      flexDirection: 'row',
      justifyContent: 'space-between',
      alignItems: 'flex-start',
      padding: 10,
    },
    text1: {
      fontSize: 16,
      lineHeight: 18,
      fontFamily: 'NotoSansCJKkr-Bold',
      marginTop: 40,
      marginBottom: 10,
    },
    headerTitle: {
      // width: 93,
      // height: 20,
      fontFamily: 'NotoSansCJKkr-Bold',
      fontSize: 20,
      color: '#1d1d1f',
    },
    header: {
      marginTop: STATUSBAR_HEIGHT,
      height: 60,
      width: '100%',
      flexDirection: 'row',
      justifyContent: 'flex-start',
      alignItems: 'center',
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
