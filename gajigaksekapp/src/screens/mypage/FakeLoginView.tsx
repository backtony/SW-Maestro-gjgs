import LoginApiController from '@/services/apis/LoginApiController';
import UserDC from '@/services/login/UserDC';
import AsyncStorage from '@react-native-async-storage/async-storage';
import React from 'react';
import {Text, TextInput} from 'react-native';
import {Button} from 'react-native-elements/dist/buttons/Button';
import {TouchableOpacity} from 'react-native-gesture-handler';
import {SafeAreaView} from 'react-native-safe-area-context';

interface FakeLoginViewStates {
  nickname: string;
}

export default class FakeLoginView extends React.Component<
  {},
  FakeLoginViewStates
> {
  constructor(props: any) {
    super(props);
    this.state = {nickname: ''};
  }

  private async postFakeLogin(username: string) {
    try {
      const res = await LoginApiController.postFakeLogin(username);

      console.log('UserDC res: ', res);

      if (res) {
        UserDC.setFakeUser(res.data);
      }

      AsyncStorage.setItem('user', JSON.stringify(UserDC.getUser()));
      this.props.navigation.navigate('mypage');
    } catch (e) {}
  }
  render() {
    return (
      <SafeAreaView>
        <Text>닉네임을 입력하세요.</Text>
        <TextInput
          style={{backgroundColor: 'gray'}}
          onChangeText={text => {
            this.setState({nickname: text});
          }}
          value={this.state.nickname}
        />
        <TouchableOpacity
          onPress={() => this.postFakeLogin(this.state.nickname)}>
          <Text>로그인 button</Text>
        </TouchableOpacity>
      </SafeAreaView>
    );
  }
}
