import React from 'react';
import {StyleSheet, Text, View, Modal} from 'react-native';
import {Button} from 'react-native-elements';
import Icon from 'react-native-vector-icons/Ionicons';

const sortOrderList = ['인기높은순', '최신순', '평점높은순', '리뷰많은순'];
const priceList = ['전체', '~5만원', '5~10만원', '10만원~'];

export default class FilterModal extends React.Component<
  {
    modalVisible: boolean;
    filterCondition: string;
    searchPriceCondition: string;
    setModalVisible: (visible: boolean) => void;
    setFilterCondition: (filterCondition: string) => void;
    setSearchPriceCondition: (searchPriceCondition: string) => void;
  },
  {filterCondition: string; searchPriceCondition: string}
> {
  constructor(props: any) {
    super(props);
    this.state = {
      filterCondition: '',
      searchPriceCondition: '',
    };
  }

  componentDidMount() {
    this.setState({
      filterCondition: this.props.filterCondition,
      searchPriceCondition: this.props.searchPriceCondition,
    });
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
            <View style={this.styles.wrapper1}>
              <Text style={{fontSize: 20, fontFamily: 'NotoSansCJKkr-Bold'}}>
                필터
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
                <Text style={this.styles.text1}>정렬 순서</Text>
                <View style={{flexDirection: 'row', flexWrap: 'wrap'}}>
                  {sortOrderList.map(value => (
                    <Button
                      title={value}
                      buttonStyle={
                        this.state.filterCondition === value
                          ? this.styles.subButtonClicked
                          : this.styles.subButton
                      }
                      titleStyle={[
                        this.styles.buttonTitle,
                        this.state.filterCondition === value
                          ? {color: '#FFF'}
                          : {},
                        ,
                      ]}
                      onPress={() => {
                        if (this.state.filterCondition === value) {
                          this.setState({filterCondition: ''});
                        } else {
                          this.setState({filterCondition: value});
                        }
                      }}
                    />
                  ))}
                </View>
                <Text style={[this.styles.text2]}>금액</Text>
                <View style={{flexDirection: 'row'}}>
                  {priceList.map(value => (
                    <Button
                      title={value}
                      buttonStyle={
                        this.state.searchPriceCondition === value
                          ? this.styles.subButtonClicked
                          : this.styles.subButton
                      }
                      titleStyle={[
                        this.styles.buttonTitle2,
                        this.state.searchPriceCondition === value
                          ? {color: '#FFF'}
                          : {},
                        ,
                      ]}
                      onPress={() => {
                        if (this.state.searchPriceCondition === value) {
                          this.setState({searchPriceCondition: ''});
                        } else {
                          this.setState({searchPriceCondition: value});
                        }
                      }}
                    />
                  ))}
                </View>
              </View>
            </View>
            <Button
              title="적용"
              buttonStyle={this.styles.button2}
              titleStyle={this.styles.buttonTitle3}
              onPress={() => {
                this.props.setFilterCondition(this.state.filterCondition);
                this.props.setSearchPriceCondition(
                  this.state.searchPriceCondition,
                );
                this.props.setModalVisible(false);
              }}
            />
          </View>
        </View>
      </Modal>
    );
  }

  private styles = StyleSheet.create({
    buttonTitle3: {
      fontSize: 16,
      lineHeight: 18,
      fontFamily: 'NotoSansCJKkr-Bold',
      color: '#fff',
    },
    button2: {
      paddingVertical: 12,
      backgroundColor: '#4f6cff',
    },
    buttonTitle2: {
      fontSize: 14,
      lineHeight: 16,
      fontFamily: 'NotoSansCJKkr-Regular',
      color: '#1d1d1f',
    },
    text2: {
      fontSize: 16,
      lineHeight: 18,
      fontFamily: 'NotoSansCJKkr-Bold',

      marginBottom: 10,
      color: '#1d1d1f',
      marginTop: 40,
    },
    buttonTitle: {
      fontSize: 14,
      lineHeight: 16,
      fontFamily: 'NotoSansCJKkr-Regular',
      color: '#1d1d1f',
    },
    text1: {
      fontSize: 16,
      lineHeight: 18,
      fontFamily: 'NotoSansCJKkr-Bold',
      marginTop: 20,
      marginBottom: 10,
      color: '#1d1d1f',
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
    header: {
      fontSize: 25,
    },
    subCategory: {
      backgroundColor: '#2980b9',
      padding: 10,
      borderRadius: 5,
      margin: 5,
      //   alignContent: 'center',
      //   justifyContent: 'center',
    },
    subCategoryText: {
      fontSize: 20,
      textAlign: 'center',
    },
    subButton: {
      marginTop: 5,
      marginRight: 10,
      backgroundColor: '#F9F9FA',
      paddingHorizontal: 17,
      borderRadius: 6,
      marginBottom: 5,
    },
    subButtonClicked: {
      marginTop: 5,
      marginRight: 10,
      paddingHorizontal: 17,
      backgroundColor: '#4f6cff',
      borderRadius: 6,
      marginBottom: 5,
    },
  });

  private modalStyles = StyleSheet.create({
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
      // flexDirection: 'row',
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
