import React from 'react';
import {StyleSheet, Text, View, TextInput, Modal} from 'react-native';
import {Button} from 'react-native-elements';
import {TouchableOpacity} from 'react-native-gesture-handler';
import Icon from 'react-native-vector-icons/Ionicons';

export default class CouponAddModal extends React.Component<
  {
    modalVisible: boolean;
    setModalVisible: (visible: boolean) => void;
    setParentSubZone: (sub: string) => void;
  },
  {selectedMainZone: string; selectedSubZone: string}
> {
  constructor(props: any) {
    super(props);
    this.state = {selectedMainZone: 'ss', selectedSubZone: 'dd'};
  }
  render() {
    return (
      <Modal
        animationType="slide"
        transparent={true}
        visible={this.props.modalVisible}
        onRequestClose={() => {
          this.props.setModalVisible(false);
        }}>
        <View style={{flex: 1}} />
        <View style={this.styles.centeredView}>
          <View style={this.styles.modalView}>
            <View style={this.styles.header}>
              <Text style={{fontSize: 20, fontFamily: 'NotoSansCJKkr-Bold'}}>
                쿠폰 등록
              </Text>
              <Button
                icon={<Icon name="close" size={24} color="#1d1d1f" />}
                buttonStyle={{backgroundColor: 'white'}}
                onPress={() => {
                  this.props.setModalVisible(false);
                }}
              />
            </View>

            <View style={{width: '100%'}}>
              <View style={this.styles.innerModalConatiner}>
                <View style={this.styles.searchWrapper}>
                  <TextInput
                    placeholder={'시리얼 번호'}
                    style={this.styles.input}
                  />
                </View>
              </View>
            </View>
            <TouchableOpacity style={this.styles.button}>
              <Text style={this.styles.text}>등록하기</Text>
            </TouchableOpacity>
          </View>
        </View>
      </Modal>
    );
  }

  private styles = StyleSheet.create({
    text: {
      fontFamily: 'NotoSansCJKkr-Bold',
      fontSize: 16,
      fontWeight: 'bold',
      fontStyle: 'normal',
      lineHeight: 20,
      letterSpacing: 0,
      color: '#ffffff',
    },
    button: {
      width: '100%',
      height: 44,
      borderRadius: 6,
      backgroundColor: '#4f6cff',
      justifyContent: 'center',
      alignItems: 'center',
    },
    centeredView: {
      flex: 13,
      justifyContent: 'center',
      alignItems: 'center',
      width: '100%',
    },
    modalView: {
      backgroundColor: 'white',
      borderRadius: 20,
      padding: 20,
      shadowColor: '#000',
      shadowOffset: {
        width: 0,
        height: 2,
      },
      shadowOpacity: 0.25,
      shadowRadius: 4,
      elevation: 5,
      width: '100%',
      flex: 2,
    },
    innerModalConatiner: {
      marginTop: 20,
    },
    header: {
      flexDirection: 'row',
      alignItems: 'center',
      justifyContent: 'space-between',
      width: '100%',
      height: 60,
      backgroundColor: '#fff',
      borderBottomWidth: 1,
      borderColor: '#f5f5f7',
    },
    searchWrapper: {
      backgroundColor: '#f5f5f7',
      width: '100%',
      padding: 10,
      borderRadius: 6,
      marginBottom: 20,
      flexDirection: 'row',
    },
    input: {
      fontSize: 14,
      color: '#1d1d1f',
    },
  });
}
