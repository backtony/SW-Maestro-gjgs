import UserDC from '@/services/login/UserDC';
import React from 'react';
import {StyleSheet, Text, TouchableOpacity, View} from 'react-native';
import {Button} from 'react-native-elements';
import Icon from 'react-native-vector-icons/Ionicons';

interface LectureDetailViewBottomProps {
  lecture: any;
  navigation: any;
  favoriteModalVisible: boolean;
  toggleFavorite: (lectureId: number) => void;
  setFavoriteModalVisible: (modalVisible: boolean) => void;
  onPressPayment: () => void;
}

export default class LectureDetailViewBottom extends React.Component<
  LectureDetailViewBottomProps,
  {}
> {
  render() {
    return (
      <View style={this.styles.container}>
        <Button
          icon={
            this.props.lecture.myFavorite ? (
              <Icon name="heart" size={30} color="#ff4f4f" />
            ) : (
              <Icon name="heart-outline" size={30} color="grey" />
            )
          }
          buttonStyle={{
            backgroundColor: 'transparent',
          }}
          onPress={() => {
            if (UserDC.isLogout()) {
              alert('로그인 해주세요.');
              return;
            }
            this.props.toggleFavorite(this.props.lecture.lectureId);
            this.props.setFavoriteModalVisible(
              !this.props.favoriteModalVisible,
            );
          }}
        />
        <TouchableOpacity
          style={this.styles.button}
          onPress={() => {
            this.props.navigation.navigate('lectureBulletin', {
              lectureId: this.props.lecture.lectureId,
            });
          }}>
          <Text style={this.styles.text1}>함께할 사람 찾기</Text>
        </TouchableOpacity>
        <TouchableOpacity
          style={this.styles.button2}
          onPress={async () => this.props.onPressPayment()}>
          <Text style={this.styles.text2}>신청하기</Text>
        </TouchableOpacity>
      </View>
    );
  }

  private styles = StyleSheet.create({
    container: {
      height: 60,
      marginHorizontal: 16,
      paddingVertical: 8,
      marginVertical: 8,
      // marginTop: 20,
      flexDirection: 'row',
    },
    button: {
      backgroundColor: '#f5f7ff',
      height: 44,
      alignItems: 'center',
      justifyContent: 'center',
      borderRadius: 6,
      marginLeft: 5,
      flex: 1,
    },
    text1: {
      // width: 93,
      // height: 20,
      fontFamily: 'NotoSansCJKkr-Bold',
      fontSize: 16,
      color: '#4f6cff',
    },
    button2: {
      backgroundColor: '#4f6cff',
      height: 44,
      alignItems: 'center',
      justifyContent: 'center',
      borderRadius: 6,
      marginLeft: 5,
      flex: 1,
    },
    text2: {
      // width: 93,
      // height: 20,
      fontFamily: 'NotoSansCJKkr-Bold',
      fontSize: 16,
      color: '#FFF',
    },
  });
}
