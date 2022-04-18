import React from 'react';
import {StyleSheet, Text, View, ScrollView, Modal} from 'react-native';
import {Button} from 'react-native-elements';
import Icon from 'react-native-vector-icons/Ionicons';
import GroupApplyListModalProfileBlock from './GroupApplyListModalProfileBlock';
import TeamManageApiController from '../../services/apis/TeamManageApiController';

export default class GroupApplyListModal extends React.Component<
  {
    modalVisible: boolean;
    teamId: number;
    setModalVisible: (visible: boolean) => void;
  },
  {appliers: any}
> {
  private beforeTeamId: number;
  private beforeModalVisible: boolean;
  constructor(props: any) {
    super(props);
    this.state = {appliers: []};
    this.beforeModalVisible = false;
  }

  componentDidUpdate() {
    if (
      this.beforeTeamId !== this.props.teamId ||
      (this.props.modalVisible && this.beforeModalVisible)
    ) {
      this.beforeModalVisible = false;
      this.getAppliers();
    }
  }

  private async getAppliers() {
    try {
      const res = await TeamManageApiController.getAppliers({
        teamId: this.props.teamId,
      });
      console.log('res: ', res);
      this.setState({appliers: res.data.applierList});
      this.beforeTeamId = this.props.teamId;
    } catch (e) {
      console.error(e);
    }
  }

  render() {
    return (
      <Modal
        animationType="slide"
        transparent={true}
        visible={this.props.modalVisible}
        onRequestClose={() => {
          this.beforeModalVisible = true;
          this.props.setModalVisible(false);
        }}>
        <View style={{flex: 1}} />
        <View style={this.styles.centeredView}>
          <View style={this.styles.modalView}>
            <View style={this.styles.header}>
              <Text style={{fontSize: 20, fontFamily: 'NotoSansCJKkr-Bold'}}>
                신청자 리스트
              </Text>
              <Button
                icon={<Icon name="close" size={24} color="#1d1d1f" />}
                buttonStyle={{backgroundColor: 'white'}}
                onPress={() => {
                  this.beforeModalVisible = true;
                  this.props.setModalVisible(false);
                }}
              />
            </View>

            <View style={{width: '100%', flex: 1}}>
              <View style={this.styles.innerModalConatiner}>
                <ScrollView>
                  {this.state.appliers &&
                    this.state.appliers.map(applier => (
                      <GroupApplyListModalProfileBlock
                        profile={{
                          nickname: applier.nickname,
                          age: applier.age,
                          gender: applier.sex,
                          thumbnailImageUrl: applier.thumbnailImageUrl,
                        }}
                        applierId={applier.memberId}
                        teamId={this.props.teamId}
                        onClicked={() => {
                          this.getAppliers();
                        }}
                      />
                    ))}
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
  });
}
