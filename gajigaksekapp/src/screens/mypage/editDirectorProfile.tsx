import React from 'react';
import {StyleSheet, Image, Text, ScrollView, TextInput} from 'react-native';
import {launchImageLibrary} from 'react-native-image-picker';
import {Button} from 'react-native-elements';

class editDirectorProfile extends React.Component<
  {},
  {
    imageBase64: string;
    nickname: string;
    categoryIdList: number[];
    isMale: boolean;
    age: number;
    modalVisible: boolean;
    selectedMainZone: string;
    selectedSubZone: string;
    profileSubZone: string;
  }
> {
  constructor(props: any) {
    super(props);
    this.state = {
      imageBase64: undefined,
      nickname: undefined,
      categoryIdList: [1, 3],
      isMale: true,
      age: 10,
      modalVisible: false,
      selectedMainZone: '서울',
      selectedSubZone: '강남/역삼/선릉/삼성',
      profileSubZone: '강남/역삼/선릉/삼성',
    };
  }

  componentDidMount() {
    const isMale = this.props.route.params.gender === 'M';
    const age = this.props.route.params.age;
    this.setState({isMale, age});
  }

  render() {
    return (
      <ScrollView>
        <Image
          style={this.styles.avatar}
          source={{
            uri: this.state.imageBase64
              ? `data:image/jpg;base64,${this.state.imageBase64}`
              : 'https://bootdey.com/img/Content/avatar/avatar6.png',
          }}
        />
        <Button
          title={'갤러리 열기'}
          onPress={() => {
            launchImageLibrary(
              {mediaType: 'photo', includeBase64: true},
              ({assets}) => {
                if (assets[0].base64) {
                  this.setState({imageBase64: assets[0].base64});
                }
              },
            );
          }}
        />
        <Text style={this.styles.header}>닉네임</Text>
        <TextInput style={this.styles.input} />
        <Text style={this.styles.header}>이름</Text>
        <TextInput
          style={this.styles.input}
          defaultValue={this.props.route.params.nickname}
        />
        <Text style={this.styles.header}>휴대폰 번호</Text>
        <TextInput style={this.styles.input} defaultValue={'010-1111-2222'} />
        <Text style={this.styles.header}>디렉터 소개</Text>
        <TextInput
          style={this.styles.bigInput}
          defaultValue={'자기소개글을 적어주세요'}
          multiline={true}
          maxLength={100}
          textAlign={'left'}
        />
      </ScrollView>
    );
  }

  private styles = StyleSheet.create({
    avatar: {
      width: 130,
      height: 130,
      borderRadius: 63,
      borderWidth: 4,
      borderColor: 'white',
      marginBottom: 10,
      alignSelf: 'center',
    },
    header: {
      fontSize: 25,
    },
    input: {
      height: 40,
      margin: 12,
      borderWidth: 1,
    },
    bigInput: {
      height: 120,
      margin: 12,
      borderWidth: 1,
    },
    genderBlockContainer: {
      flexDirection: 'row',
      justifyContent: 'space-around',
    },
    genderText: {
      fontSize: 20,
    },
    genderBlockSelected: {
      width: '40%',
      height: 40,
      padding: 10,
      backgroundColor: '#bdc3c7',
      margin: 10,
      borderRadius: 5,
      alignItems: 'center',
      justifyContent: 'center',
    },
    genderBlock: {
      height: 40,
      padding: 10,
      width: '40%',
      backgroundColor: '#ecf0f1',
      margin: 10,
      borderRadius: 5,
      alignItems: 'center',
      justifyContent: 'center',
    },
  });
}

export default editDirectorProfile;
