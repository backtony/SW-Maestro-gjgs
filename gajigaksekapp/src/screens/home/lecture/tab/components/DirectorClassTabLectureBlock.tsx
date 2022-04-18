import React from 'react';
import {Image, StyleSheet, Text, TouchableOpacity, View} from 'react-native';
import Icon from 'react-native-vector-icons/Ionicons';
import {Button} from 'react-native-elements';
import DropDownPicker from 'react-native-dropdown-picker';
import Zone from '@/utils/Zone';

interface DirectorClassTabLectureBlockProps {
  lecture: any;
  navigation: any;
  onPressHeart: () => void;
}

interface DirectorClassTabLectureStates {
  dropDown: boolean;
}

export default class DirectorClassTabLectureBlock extends React.Component<
  DirectorClassTabLectureBlockProps,
  DirectorClassTabLectureStates
> {
  constructor(props: any) {
    super(props);
    this.state = {dropDown: false};
  }
  render() {
    return (
      <View style={styles.card}>
        <TouchableOpacity
          onPress={() => {
            this.props.navigation.navigate('lectureDash2', {
              lectureId: this.props.lecture.lectureId,
              myFavorite: this.props.lecture.myFavorite,
              lecture: this.props.lecture,
            });
          }}>
          <View style={styles.imageContainer}>
            <Image
              style={styles.cardImage}
              source={{uri: this.props.lecture.imageUrl}}
            />
            <View style={styles.locationWrapper}>
              <Icon name="location-sharp" size={12} color="#FFF" />

              <Text style={styles.zone}>
                {Zone.getZone(this.props.lecture.zoneId)[1]}
              </Text>
            </View>
            <View style={{position: 'absolute', right: 0}}>
              <Button
                icon={
                  this.props.lecture.myFavorite ? (
                    <Icon name="heart" size={30} color="#ff4f4f" />
                  ) : (
                    <Icon name="heart-outline" size={30} color="#FFF" />
                  )
                }
                buttonStyle={{
                  backgroundColor: 'transparent',
                }}
                onPress={() => this.props.onPressHeart()}
              />
            </View>
          </View>

          <View style={styles.cardContent}>
            <Text style={styles.title}>{this.props.lecture.title}</Text>
          </View>
        </TouchableOpacity>
        <DropDownPicker
          open={this.state.dropDown}
          value={'one'}
          items={[
            {
              label: `${this.props.lecture.priceOne}원`,
              value: 'one',
              icon: () => <Text style={{fontSize: 12}}>1인</Text>,
            },
            {
              label: `${this.props.lecture.priceTwo}원`,
              value: 'two',
              icon: () => <Text style={{fontSize: 12}}>2인</Text>,
            },
            {
              label: `${this.props.lecture.priceThree}원`,
              value: 'three',
              icon: () => <Text style={{fontSize: 12}}>3인</Text>,
            },
            {
              label: `${this.props.lecture.priceFour}원`,
              value: 'four',
              icon: () => <Text style={{fontSize: 12}}>4인 이상</Text>,
            },
          ]}
          setOpen={() => this.setState({dropDown: !this.state.dropDown})}
          setValue={callback => {}}
          setItems={() => {}}
          showTickIcon={false}
          dropDownDirection="TOP"
          style={styles.dropDown}
          listItemContainerStyle={{height: 28}}
          listItemLabelStyle={{color: '#4a4a4c', fontSize: 12}} //dd
          dropDownContainerStyle={{
            borderWidth: 0,
          }}
          labelProps={{
            numberOfLines: 1,
          }}
          arrowIconStyle={{
            width: 18,
            height: 18,
          }}
          textStyle={{fontSize: 14, fontWeight: 'bold'}}
        />
      </View>
    );
  }
}

const styles = StyleSheet.create({
  dropDown: {
    backgroundColor: '#fafafb',
    borderWidth: 0,
    height: 34,
  },
  locationWrapper: {
    position: 'absolute',
    bottom: 0,
    flexDirection: 'row',
    borderRadius: 9,
    backgroundColor: 'rgba(0, 0, 0, 0.3)',
    padding: 2,
    paddingHorizontal: 5,
    margin: 8,
  },
  subCategory: {
    backgroundColor: '#2980b9',
    padding: 10,
    borderRadius: 5,
    margin: 5,
    alignContent: 'center',
    justifyContent: 'center',
  },
  subCategoryText: {
    fontSize: 20,
    textAlign: 'center',
  },
  container: {
    flex: 1,
    backgroundColor: '#FFF',
  },
  list: {
    paddingHorizontal: 10,
  },
  listContainer: {
    marginHorizontal: 20,
    marginTop: 10,
  },
  separator: {
    marginTop: 10,
  },
  /******** card **************/
  card: {
    // marginVertical: 8,
    backgroundColor: 'white',
    width: 152,
    marginBottom: 15,
  },
  cardContent: {
    paddingTop: 7,
    paddingLeft: 4,
    justifyContent: 'space-between',
  },
  cardImage: {
    flex: 1,
    height: 122,
    borderRadius: 9,
  },
  imageContainer: {
    borderRadius: 10,
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
    marginBottom: 4,
  },
  age: {
    fontFamily: 'NotoSansCJKkr-Regular',
    fontSize: 12,
    lineHeight: 14,
    color: '#4a4a4c',
    marginBottom: 4,
  },
  time: {
    fontSize: 18,
    flex: 1,
    color: '#778899',
  },
  people: {
    fontFamily: 'NotoSansCJKkr-Regular',
    fontSize: 14,
    lineHeight: 16,
    color: '#4a4a4c',
  },
  peopleBold: {
    fontFamily: 'NotoSansCJKkr-Bold',
    fontSize: 14,
    lineHeight: 16,
    color: '#4f6cff',
    marginLeft: 5,
  },
  count: {
    fontSize: 18,
    flex: 1,
    color: '#B0C4DE',
  },
});
