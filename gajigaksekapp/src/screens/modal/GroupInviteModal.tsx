import React from 'react';
import {
  StyleSheet,
  Text,
  View,
  ScrollView,
  TextInput,
  Modal,
} from 'react-native';
import {Button} from 'react-native-elements';
import Icon from 'react-native-vector-icons/Ionicons';
import GroupInviteModalProfileBlock from '../group/GroupInviteModalProfileBlock';

export default class GroupInviteModal extends React.Component<
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
                그룹 초대하기
              </Text>
              <Button
                icon={<Icon name="close" size={24} color="#1d1d1f" />}
                buttonStyle={{backgroundColor: 'white'}}
                onPress={() => {
                  this.props.setModalVisible(false);
                }}
              />
            </View>

            <View style={{width: '100%', flex: 1}}>
              <View style={this.styles.innerModalConatiner}>
                <View style={this.styles.searchWrapper}>
                  <Icon
                    name="search"
                    size={24}
                    color={'#8e8e8f'}
                    style={{marginRight: 8}}
                  />
                  <TextInput
                    placeholder={'닉네임 검색'}
                    style={this.styles.input}
                  />
                </View>
                <ScrollView>
                  <GroupInviteModalProfileBlock
                    profile={{
                      nickname: '팝콘하우스',
                      age: 27,
                      gender: 'M',
                    }}
                    memberEdit={false}
                    memberAdd={true}
                  />
                  <GroupInviteModalProfileBlock
                    profile={{
                      nickname: '김은혜',
                      age: 26,
                      gender: 'F',
                    }}
                    memberEdit={false}
                    memberAdd={true}
                  />
                  <GroupInviteModalProfileBlock
                    profile={{
                      nickname: '김지수',
                      age: 32,
                      gender: 'M',
                    }}
                    memberEdit={false}
                    memberAdd={true}
                  />
                </ScrollView>
              </View>
            </View>
          </View>
        </View>
      </Modal>
    );
  }

  private styles = StyleSheet.create({
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
      color: '#d2d2d2',
    },
  });
}
