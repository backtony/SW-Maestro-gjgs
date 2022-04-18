import React from 'react';
import {StyleSheet, Text, View, Modal} from 'react-native';
import {Button} from 'react-native-elements';
import Icon from 'react-native-vector-icons/Ionicons';
import {SceneMap, TabBar, TabView} from 'react-native-tab-view';
import BulletinLectureSelectLectureTab from './BulletinLectureSelectLectureTab';
import BulletinLectureSelectGroupTab from './BulletinLectureSelectGroupTab';

export default class BulletinLectureSelectModal extends React.Component<
  {
    modalVisible: boolean;
    setModalVisible: (visible: boolean) => void;
    setParentLectureId: (lectureId: number) => void;
  },
  {tabIndex: number; selectedLectureId: number}
> {
  constructor(props: any) {
    super(props);
    this.state = {tabIndex: 0, selectedLectureId: -1};
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
            <View style={this.styles.textWrapper}>
              <Text style={this.styles.title}>클래스 선택하기</Text>
              <Button
                icon={<Icon name="close" size={24} color="#1d1d1f" />}
                buttonStyle={{backgroundColor: 'white'}}
                onPress={() => {
                  this.props.setModalVisible(false);
                }}
              />
            </View>

            <View style={{flex: 1, backgroundColor: '#FFF'}}>
              <View style={{backgroundColor: '#FFF', flex: 1}}>
                <TabView
                  renderTabBar={props => (
                    <TabBar
                      style={{backgroundColor: 'white'}}
                      indicatorStyle={this.styles.indicator}
                      renderLabel={({route, focused, color}) => (
                        <Text
                          style={[
                            this.styles.text,
                            focused ? {color: '#4f6cff'} : {},
                          ]}>
                          {route.title}
                        </Text>
                      )}
                      {...props}
                    />
                  )}
                  style={{flex: 1, backgroundColor: 'red'}}
                  navigationState={{
                    index: 0,
                    routes: [
                      {key: 'class', title: '클래스'},
                      {key: 'group', title: '그룹'},
                    ],
                  }}
                  onIndexChange={index => {
                    this.setState({tabIndex: index});
                  }}
                  renderScene={SceneMap({
                    class: () => (
                      <BulletinLectureSelectLectureTab
                        selectedLectureId={this.state.selectedLectureId}
                        onSelectLecture={(lectureId: number) => {
                          if (this.state.selectedLectureId === lectureId) {
                            this.setState({selectedLectureId: -1});
                          } else {
                            this.setState({selectedLectureId: lectureId});
                          }
                        }}
                      />
                    ),
                    group: () => (
                      <BulletinLectureSelectGroupTab
                        selectedLectureId={this.state.selectedLectureId}
                        onSelectLecture={(lectureId: number) => {
                          if (this.state.selectedLectureId === lectureId) {
                            this.setState({selectedLectureId: -1});
                          } else {
                            this.setState({selectedLectureId: lectureId});
                          }
                        }}
                      />
                    ),
                  })}
                />
              </View>
            </View>

            <Button
              buttonStyle={this.styles.button}
              titleStyle={this.styles.buttonTitle}
              title="선택 완료"
              onPress={() => {
                this.props.setParentLectureId(this.state.selectedLectureId);
                this.props.setModalVisible(false);
              }}
            />
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
    textWrapper: {
      flexDirection: 'row',
      alignItems: 'center',
      justifyContent: 'space-between',
      width: '100%',
      height: 60,
      backgroundColor: '#fff',
      borderBottomWidth: 1,
      borderColor: '#f5f5f7',
    },
    indicator: {
      backgroundColor: '#4f6cff',
      height: 4,
      width: 40,
      alignSelf: 'center',
      marginLeft: 68,
      borderRadius: 2,
    },
    text: {
      fontSize: 16,
      lineHeight: 18,
      fontFamily: 'NotoSansCJKkr-Regular',
      color: '#8e8e8f',
    },
    button: {
      marginTop: 8,
      borderRadius: 6,
      height: 44,
      backgroundColor: '#4f6cff',
    },
    buttonTitle: {
      fontSize: 16,
      lineHeight: 18,
      fontFamily: 'NotoSansCJKkr-Bold',
    },
    title: {fontSize: 20, fontFamily: 'NotoSansCJKkr-Bold'},
  });
}
