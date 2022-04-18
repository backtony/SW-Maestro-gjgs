import React from 'react';
import {
  StyleSheet,
  Text,
  View,
  ScrollView,
  Modal,
  TouchableOpacity,
} from 'react-native';
import {Button} from 'react-native-elements';
import Icon from 'react-native-vector-icons/Ionicons';
import Category from '../../utils/Category';
import FirstSubCategoryContainer from '../mypage/firstLogin/FirstSubCategoryContainer';

export default class CategorySelectModal extends React.Component<
  {
    modalVisible: boolean;
    setModalVisible: (visible: boolean) => void;
    setSubCategory: (sub: string[]) => void;
    selectedCategoryList: string[];
  },
  {subCategoryIdList: number[]}
> {
  constructor(props: any) {
    super(props);
    this.state = {
      subCategoryIdList: props.selectedCategoryList.map((item: any) =>
        Category.getIdWithSub(item),
      ),
    };
  }

  componentDidUpdate(prevProps: any) {
    if (this.props.selectedCategoryList !== prevProps.selectedCategoryList) {
      this.setState({
        subCategoryIdList: this.props.selectedCategoryList.map((item: any) =>
          Category.getIdWithSub(item),
        ),
      });
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
            <View style={this.modalStyles.wrapper1}>
              <Text style={{fontSize: 20, fontFamily: 'NotoSansCJKkr-Bold'}}>
                카테고리 선택
              </Text>
              <Button
                icon={<Icon name="close" size={24} color="#1d1d1f" />}
                buttonStyle={{backgroundColor: 'white'}}
                onPress={() => {
                  this.props.setModalVisible(false);
                }}
              />
            </View>
            <ScrollView>
              {Category.getCategoryList().map(item => (
                <FirstSubCategoryContainer
                  key={item}
                  mainCategory={item}
                  subCategoryIdList={this.state.subCategoryIdList}
                  updateSubCategoryList={(subCategoryIdList: number[]) => {
                    this.setState({subCategoryIdList});
                  }}
                />
              ))}
              <View style={{height: 115}} />
            </ScrollView>

            <View>
              <ScrollView horizontal={true} style={this.modalStyles.scrollview}>
                {this.state.subCategoryIdList.map(id => (
                  <View style={this.modalStyles.wrapper2}>
                    <TouchableOpacity
                      style={{marginRight: 5}}
                      onPress={() => {
                        let updatedList = [...this.state.subCategoryIdList, id];

                        if (this.state.subCategoryIdList.includes(id)) {
                          updatedList = updatedList.filter(
                            value => value !== id,
                          );
                          this.setState({subCategoryIdList: updatedList});
                          return;
                        }

                        this.setState({subCategoryIdList: updatedList});
                      }}>
                      <Icon name="close-circle" size={16} color="#4f6cff" />
                    </TouchableOpacity>
                    <Text style={this.modalStyles.text1}>
                      {Category.getCategory(id)[1]}
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
                this.props.setSubCategory(
                  this.state.subCategoryIdList.map(
                    item => Category.getCategory(item)[1],
                  ),
                );
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
    text1: {
      fontSize: 14,
      color: '#4f6cff',
    },
    wrapper2: {
      flexDirection: 'row',
      alignItems: 'center',
      backgroundColor: '#f5f7ff',
      paddingHorizontal: 8,
      borderRadius: 6,
      height: 28,
      marginRight: 5,
    },
    scrollview: {
      height: 48,
      flexDirection: 'row',
      padding: 10,
      paddingLeft: 0,
    },
    wrapper1: {
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
