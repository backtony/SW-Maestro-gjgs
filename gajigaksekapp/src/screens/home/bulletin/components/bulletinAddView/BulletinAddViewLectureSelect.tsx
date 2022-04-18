import BulletinLectureSelectModal from '@/screens/modal/BulletinLectureSelectModal';
import Zone from '@/utils/Zone';
import React from 'react';
import {Image, StyleSheet, Text, TouchableOpacity, View} from 'react-native';
import DropDownPicker from 'react-native-dropdown-picker';
import Icon from 'react-native-vector-icons/Ionicons';

interface BulletinAddViewLectureSelectProps {
  lectureInfo: any;
  getLecture: (lectureId: number) => void;
}

interface BulletinAddViewLectureSelectStates {
  modalVisible: boolean;
  dropdown: boolean;
}

export default class BulletinAddViewLectureSelect extends React.Component<
  BulletinAddViewLectureSelectProps,
  BulletinAddViewLectureSelectStates
> {
  constructor(props: any) {
    super(props);
    this.state = {modalVisible: false, dropdown: false};
  }
  render() {
    return (
      <View>
        <View style={this.styles.header}>
          <View>
            <Text style={this.styles.headerText}>하고 싶은 클래스</Text>
          </View>
        </View>
        <View style={this.styles.card}>
          <View style={this.styles.imageContainer}>
            <Image
              style={this.styles.cardImage}
              source={{
                uri: this.props.lectureInfo.thumbnailImageUrl,
              }}
            />
          </View>
          <View style={{width: 200}}>
            <View style={this.styles.cardContent}>
              <Text style={this.styles.title}>
                {this.props.lectureInfo.lectureTitle}
              </Text>
            </View>

            <View style={this.styles.locationWrapper}>
              <Icon name="location-sharp" size={12} color="#FFF" />
              <Text style={this.styles.zone}>
                {Zone.getZone(this.props.lectureInfo.zoneId)[1]}
              </Text>
            </View>

            <DropDownPicker
              open={this.state.dropdown}
              value={'one'}
              items={[
                {
                  label: `${this.props.lectureInfo.priceOne}원`,
                  value: 'one',
                  icon: () => <Text style={{fontSize: 12}}>1인</Text>,
                },
                {
                  label: `${this.props.lectureInfo.priceTwo}원`,
                  value: 'two',
                  icon: () => <Text style={{fontSize: 12}}>2인</Text>,
                },
                {
                  label: `${this.props.lectureInfo.priceThree}원`,
                  value: 'three',
                  icon: () => <Text style={{fontSize: 12}}>3인</Text>,
                },
                {
                  label: `${this.props.lectureInfo.priceFour}원`,
                  value: 'four',
                  icon: () => <Text style={{fontSize: 12}}>4인 이상</Text>,
                },
              ]}
              setOpen={() => {
                this.setState({dropdown: !this.state.dropdown});
              }}
              setValue={() => {}}
              setItems={() => {}}
              showTickIcon={false}
              dropDownDirection="TOP"
              style={this.styles.dropDown}
              listItemContainerStyle={{height: 28}}
              listItemLabelStyle={{color: '#4a4a4c', fontSize: 12}} //dd
              dropDownContainerStyle={this.styles.dropDownContainer}
              arrowIconStyle={this.styles.arrowIcon}
              textStyle={{fontSize: 14, fontWeight: 'bold'}}
            />
          </View>
        </View>
        <TouchableOpacity
          style={this.styles.button}
          onPress={() => {
            this.setState({
              modalVisible: !this.state.modalVisible,
            });
          }}>
          <Text style={this.styles.buttonText}>+ 찜에서 가져오기</Text>
        </TouchableOpacity>

        <View>
          <BulletinLectureSelectModal
            modalVisible={this.state.modalVisible}
            setModalVisible={(visible: boolean) => {
              this.setState({modalVisible: visible});
            }}
            setParentLectureId={(lectureId: number) => {
              this.props.getLecture(lectureId);
            }}
          />
        </View>
      </View>
    );
  }
  private styles = StyleSheet.create({
    /******** card **************/
    card: {
      marginBottom: 15,
      flexDirection: 'row',
    },
    cardContent: {
      paddingLeft: 4,
    },
    cardImage: {
      flex: 1,
      height: 122,
      width: 122,
      borderRadius: 9,
    },
    imageContainer: {
      borderRadius: 10,
      width: 120,
      height: 96,
      marginRight: 16,
    },
    /******** card components **************/
    zone: {
      fontSize: 10,
      color: '#FFF',
    },
    title: {
      fontFamily: 'NotoSansCJKkr-Bold',
      fontSize: 14,
      lineHeight: 16,
      color: '#070707',
    },
    header: {
      flexDirection: 'row',
      justifyContent: 'space-between',
      alignItems: 'center',

      marginTop: 40,
      marginBottom: 10,
    },
    headerText: {
      fontSize: 16,
      lineHeight: 18,
      fontFamily: 'NotoSansCJKkr-Bold',
    },
    locationWrapper: {
      bottom: 0,
      flexDirection: 'row',
      borderRadius: 9,
      backgroundColor: 'rgba(0, 0, 0, 0.3)',
      padding: 2,
      paddingHorizontal: 5,
      marginVertical: 5,
      alignSelf: 'flex-start',
    },
    dropDown: {
      backgroundColor: '#fafafb',
      borderWidth: 0,
      height: 34,
    },
    dropDownContainer: {
      borderWidth: 0,
    },
    arrowIcon: {
      width: 18,
      height: 18,
    },
    button: {
      backgroundColor: '#4f6cff',
      height: 44,
      alignItems: 'center',
      justifyContent: 'center',
      borderRadius: 6,
    },
    buttonText: {
      fontFamily: 'NotoSansCJKkr-Bold',
      fontSize: 16,
      color: '#FFF',
    },
  });
}
