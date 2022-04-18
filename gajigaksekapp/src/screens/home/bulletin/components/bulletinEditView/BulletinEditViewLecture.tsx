import React from 'react';
import {Image, StyleSheet, Text, TouchableOpacity, View} from 'react-native';
import DropDownPicker from 'react-native-dropdown-picker';
import Icon from 'react-native-vector-icons/Ionicons';

interface BulletinEditViewLectureProps {}

interface BulletinEditViewLectureStates {
  dropDown: boolean;
}

export default class BulletinEditViewLecture extends React.Component<
  BulletinEditViewLectureProps,
  BulletinEditViewLectureStates
> {
  constructor(props: any) {
    super(props);
    this.state = {dropDown: false};
  }
  render() {
    return (
      <View>
        <View style={this.styles.titleWrapper}>
          <View>
            <Text style={this.styles.title2}>하고 싶은 클래스</Text>
            <Text style={this.styles.text1}>
              클래스를 정하지 못했다면 선택하지 않아도 됩니다.
            </Text>
          </View>
        </View>

        <View style={this.styles.card}>
          <View style={this.styles.imageContainer}>
            <Image
              style={this.styles.cardImage}
              source={{
                uri: 'https://via.placeholder.com/400x200/FFB6C1/000000',
              }}
            />
          </View>
          <View style={{backgroundColor: 'transparent', width: 200}}>
            <View style={this.styles.cardContent}>
              <Text style={this.styles.title}>
                16년차 베이커리 장인의 케이크 클래스
              </Text>
            </View>

            <View style={this.styles.locationWrapper}>
              <Icon name="location-sharp" size={12} color="#FFF" />
              <Text style={this.styles.zone}>홍대</Text>
            </View>

            <DropDownPicker
              open={this.state.dropDown}
              value={'one'}
              items={[
                {
                  label: '50000원',
                  value: 'one',
                  icon: () => <Text style={{fontSize: 12}}>1인</Text>,
                },
                {
                  label: '49500원',
                  value: 'two',
                  icon: () => <Text style={{fontSize: 12}}>2인</Text>,
                },
                {
                  label: '47500원',
                  value: 'three',
                  icon: () => <Text style={{fontSize: 12}}>3인</Text>,
                },
                {
                  label: '46500원',
                  value: 'four',
                  icon: () => <Text style={{fontSize: 12}}>4인 이상</Text>,
                },
              ]}
              setOpen={() => {
                this.setState({dropDown: !this.state.dropDown});
              }}
              setValue={() => {}}
              setItems={() => {}}
              showTickIcon={false}
              dropDownDirection="TOP"
              style={this.styles.dropDown}
              listItemContainerStyle={{height: 28}}
              listItemLabelStyle={{color: '#4a4a4c', fontSize: 12}} //dd
              dropDownContainerStyle={{
                borderWidth: 0,
              }}
              arrowIconStyle={{
                width: 18,
                height: 18,
              }}
              textStyle={{fontSize: 14, fontWeight: 'bold'}}
            />
          </View>
        </View>
        <TouchableOpacity
          style={this.styles.button2}
          onPress={() => {
            // this.createGroupApi();
          }}>
          <Text style={this.styles.buttonText}>+ 찜에서 가져오기</Text>
        </TouchableOpacity>
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
    titleWrapper: {
      flexDirection: 'row',
      justifyContent: 'space-between',
      alignItems: 'center',

      marginTop: 40,
      marginBottom: 10,
    },
    title2: {
      fontSize: 16,
      lineHeight: 18,
      fontFamily: 'NotoSansCJKkr-Bold',
    },
    text1: {
      fontSize: 12,
      lineHeight: 14,
      fontFamily: 'NotoSansCJKkr-Regular',
      color: '#8e8e8f',
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
    button2: {
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
