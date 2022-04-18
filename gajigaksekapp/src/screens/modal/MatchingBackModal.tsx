import React from 'react';
import {
  StyleSheet,
  Image,
  Text,
  View,
  Modal,
  TouchableOpacity,
} from 'react-native';

export default class MatchingBackModal extends React.Component<
  {
    modalVisible: boolean;
    setModalVisible: (visible: boolean) => void;
    setParentSubZone: (sub: string) => void;
    navigation: any;
  },
  {selectedMainZone: string; selectedSubZone: string}
> {
  constructor(props: any) {
    super(props);
    this.state = {
      selectedMainZone: '서울',
      selectedSubZone: '강남/역삼/선릉/삼성',
    };
  }
  render() {
    return (
      <Modal
        animationType="fade"
        transparent={true}
        visible={this.props.modalVisible}
        onRequestClose={() => {
          this.props.setModalVisible(false);
        }}>
        <View style={this.modalStyles.centeredView}>
          <View style={this.modalStyles.modalView}>
            <View style={this.modalStyles.wrapper}>
              <Image
                style={{width: 48, height: 48}}
                source={require('gajigaksekapp/src/asset/iconImage/handcraft2.png')}
              />
            </View>
            <Text style={this.modalStyles.text1}>매칭을 중단</Text>
            <Text style={this.modalStyles.text2}>하시겠습니까?</Text>
            <TouchableOpacity
              style={this.modalStyles.button3}
              onPress={() => {
                this.props.setModalVisible(false);
                this.props.navigation.navigate('bulletinMain');
              }}>
              <Text style={this.modalStyles.text3}>중단</Text>
            </TouchableOpacity>
            <TouchableOpacity
              style={this.modalStyles.button4}
              onPress={() => {
                this.props.setModalVisible(false);
              }}>
              <Text style={this.modalStyles.text4}>취소</Text>
            </TouchableOpacity>
          </View>
        </View>
      </Modal>
    );
  }

  private modalStyles = StyleSheet.create({
    text4: {
      fontSize: 16,
      lineHeight: 20,
      fontFamily: 'NotoSansCJKkr-Bold',
      color: '#8e8e8f',
    },
    button4: {
      flexDirection: 'row',

      padding: 12,
      width: 240,
      borderRadius: 100,
      alignItems: 'center',
      justifyContent: 'center',
      marginTop: 10,
    },
    text3: {
      fontSize: 16,
      lineHeight: 20,
      fontFamily: 'NotoSansCJKkr-Bold',
      color: '#FFF',
    },
    button3: {
      flexDirection: 'row',
      backgroundColor: '#ff4f4f',
      padding: 12,
      width: 240,
      borderRadius: 100,
      alignItems: 'center',
      justifyContent: 'center',
      marginTop: 12,

      shadowColor: '#4c4f6cff',
      shadowOffset: {
        width: 0,
        height: 4,
      },
      shadowOpacity: 0.29,
      shadowRadius: 4.65,

      elevation: 7,
    },
    text2: {
      fontSize: 20,
      lineHeight: 24,
      fontFamily: 'NotoSansCJKkr-Bold',
    },
    text1: {
      fontSize: 20,
      lineHeight: 24,
      fontFamily: 'NotoSansCJKkr-Bold',
      marginTop: 20,
    },
    wrapper: {
      backgroundColor: '#f5f5f7',
      padding: 16,
      borderRadius: 100,
      width: 80,
      height: 80,
    },
    centeredView: {
      flex: 13,
      justifyContent: 'center',
      alignItems: 'center',
      width: '100%',
      backgroundColor: 'rgba(52, 52, 52, 0.8)',
    },
    modalView: {
      // margin: 20,
      alignItems: 'center',

      backgroundColor: '#FFF',
      borderRadius: 20,
      padding: 20,
      // alignItems: 'center',
      shadowColor: '#000',
      shadowOffset: {
        width: 0,
        height: 2,
      },
      shadowOpacity: 0.25,
      shadowRadius: 4,
      elevation: 5,
      //   width: '100%',
      //   flex: 2,
    },
    button: {
      borderRadius: 20,
      padding: 10,
      elevation: 2,
    },
    buttonOpen: {
      backgroundColor: '#F194FF',
    },
    buttonClose: {
      backgroundColor: '#2196F3',
    },
    textStyle: {
      color: 'white',
      fontWeight: 'bold',
      textAlign: 'center',
    },
    modalText: {
      marginBottom: 15,
      textAlign: 'center',
    },
    innerModalConatiner: {
      flexDirection: 'row',
      marginTop: 20,
    },
    scrollView: {
      width: '50%',
    },
    buttonText: {
      fontSize: 12,
      fontFamily: 'NotoSansCJKkr-Bold',
      lineHeight: 14,
      color: '#fff',
    },
    button2: {
      height: 40,
      borderRadius: 8,
      justifyContent: 'center',
      paddingLeft: 12,
      marginBottom: 10,
    },
    buttonOn: {backgroundColor: '#4f6cff'},
    buttonOff: {backgroundColor: '#fafafb'},
  });
}
