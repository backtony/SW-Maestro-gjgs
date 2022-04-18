import React from 'react';
import {
  StyleSheet,
  Text,
  View,
  ScrollView,
  Modal,
  TouchableOpacity,
} from 'react-native';
import Zone from '../../utils/Zone';
import {Button} from 'react-native-elements';
import Icon from 'react-native-vector-icons/Ionicons';

export default class MultiZoneSelectModal extends React.Component<
  {
    modalVisible: boolean;
    setModalVisible: (visible: boolean) => void;
    setParentSubZone: (sub: string[]) => void;
  },
  {selectedMainZone: string; selectedSubZone: string[]}
> {
  constructor(props: any) {
    super(props);
    this.state = {
      selectedMainZone: '서울',
      selectedSubZone: ['강남/역삼/선릉/삼성'],
    };
  }

  private onChangeSubCategory(sub: string) {
    let updatedList = [...this.state.selectedSubZone, sub];

    if (this.state.selectedSubZone.includes(sub)) {
      updatedList = updatedList.filter(value => value !== sub);
      this.setState({selectedSubZone: updatedList});
      return;
    }

    if (this.state.selectedSubZone.length < 3) {
      this.setState({selectedSubZone: updatedList});
    }
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
        <View style={this.modalStyles.centeredView}>
          <View style={this.modalStyles.modalView}>
            <View style={this.modalStyles.wrapper}>
              <Text style={{fontSize: 20, fontFamily: 'NotoSansCJKkr-Bold'}}>
                지역
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
              <View style={this.modalStyles.innerModalConatiner}>
                <View style={this.modalStyles.wrapper2}>
                  <TouchableOpacity
                    style={[
                      this.state.selectedMainZone === '서울'
                        ? this.modalStyles.buttonOn
                        : this.modalStyles.buttonOff,
                      this.modalStyles.button2,
                    ]}
                    onPress={() => {
                      this.setState({selectedMainZone: '서울'});
                    }}>
                    <Text
                      style={[
                        this.modalStyles.buttonText,
                        this.state.selectedMainZone === '서울'
                          ? {}
                          : {color: '#4a4a4c'},
                      ]}>
                      서울
                    </Text>
                  </TouchableOpacity>
                  <TouchableOpacity
                    style={[
                      this.modalStyles.button2,
                      this.state.selectedMainZone === '경기'
                        ? this.modalStyles.buttonOn
                        : this.modalStyles.buttonOff,
                      ,
                    ]}
                    onPress={() => {
                      this.setState({selectedMainZone: '경기'});
                    }}>
                    <Text
                      style={[
                        this.modalStyles.buttonText,
                        this.state.selectedMainZone === '경기'
                          ? {}
                          : {color: '#4a4a4c'},
                      ]}>
                      경기
                    </Text>
                  </TouchableOpacity>
                  <TouchableOpacity
                    style={[
                      this.modalStyles.button2,
                      this.state.selectedMainZone === '인천'
                        ? this.modalStyles.buttonOn
                        : this.modalStyles.buttonOff,
                    ]}
                    onPress={() => {
                      this.setState({selectedMainZone: '인천'});
                    }}>
                    <Text
                      style={[
                        this.modalStyles.buttonText,
                        this.state.selectedMainZone === '인천'
                          ? {}
                          : {color: '#4a4a4c'},
                      ]}>
                      인천
                    </Text>
                  </TouchableOpacity>
                </View>
                <ScrollView
                  style={{marginLeft: 20}}
                  showsVerticalScrollIndicator={false}
                  showsHorizontalScrollIndicator={false}>
                  {Zone.getSubZoneList(this.state.selectedMainZone).map(
                    value => (
                      <TouchableOpacity
                        key={value.id}
                        style={[
                          this.modalStyles.button2,
                          this.state.selectedSubZone.includes(value.sub)
                            ? this.modalStyles.buttonOn
                            : this.modalStyles.buttonOff,
                        ]}
                        onPress={() => {
                          this.onChangeSubCategory(value.sub);
                        }}>
                        <Text
                          style={[
                            this.modalStyles.buttonText,
                            this.state.selectedSubZone.includes(value.sub)
                              ? {}
                              : {color: '#4a4a4c'},
                          ]}>
                          {value.sub}
                        </Text>
                      </TouchableOpacity>
                    ),
                  )}
                </ScrollView>
              </View>
            </View>
            <View>
              <ScrollView
                horizontal={true}
                style={this.modalStyles.scrollView2}
                showsVerticalScrollIndicator={false}
                showsHorizontalScrollIndicator={false}>
                {this.state.selectedSubZone.map(sub => (
                  <View key={sub} style={this.modalStyles.wrapper3}>
                    <TouchableOpacity
                      style={{marginRight: 5}}
                      onPress={() => {
                        let updatedList = [...this.state.selectedSubZone, sub];

                        if (this.state.selectedSubZone.includes(sub)) {
                          updatedList = updatedList.filter(
                            value => value !== sub,
                          );
                          this.setState({selectedSubZone: updatedList});
                          return;
                        }

                        this.setState({selectedSubZone: updatedList});
                      }}>
                      <Icon name="close-circle" size={16} color="#4f6cff" />
                    </TouchableOpacity>
                    <Text
                      style={[this.modalStyles.buttonText, {color: '#4f6cff'}]}>
                      {sub}
                    </Text>
                  </View>
                ))}
              </ScrollView>
            </View>

            <Button
              buttonStyle={this.modalStyles.button3}
              titleStyle={this.modalStyles.buttonTitle}
              title="확인"
              onPress={() => {
                this.props.setParentSubZone(this.state.selectedSubZone);
                this.props.setModalVisible(false);
              }}
            />
          </View>
        </View>
      </Modal>
    );
  }

  private modalStyles = StyleSheet.create({
    buttonTitle: {
      fontSize: 16,
      lineHeight: 18,
      fontFamily: 'NotoSansCJKkr-Bold',
    },
    button3: {
      marginTop: 8,
      borderRadius: 6,
      height: 44,
      backgroundColor: '#4f6cff',
    },
    wrapper3: {
      height: 28,
      backgroundColor: '#f5f7ff',
      alignItems: 'center',
      justifyContent: 'center',
      paddingHorizontal: 10,
      borderRadius: 6,
      marginLeft: 10,
      flexDirection: 'row',
    },
    scrollView2: {
      paddingVertical: 10,
      height: 48,
      borderTopWidth: 1,
      borderTopColor: '#f5f5f7',
    },
    wrapper2: {
      width: '50%',
      paddingRight: 20,
      borderRightWidth: 1,
      borderColor: '#f5f5f7',
    },
    wrapper: {
      flexDirection: 'row',
      alignItems: 'center',
      justifyContent: 'space-between',
      width: '100%',
      height: 60,
      backgroundColor: '#fff',
      borderBottomWidth: 1,
      borderColor: '#f5f5f7',
    },
    centeredView: {
      flex: 13,
      justifyContent: 'center',
      alignItems: 'center',
      width: '100%',
    },
    modalView: {
      // margin: 20,

      backgroundColor: 'white',
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
      width: '100%',
      flex: 2,
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
