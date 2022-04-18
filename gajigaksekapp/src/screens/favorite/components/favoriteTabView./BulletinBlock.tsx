import React from 'react';
import {Alert, Image, Text, TouchableOpacity, View} from 'react-native';
import Icon from 'react-native-vector-icons/Ionicons';
import {Button} from 'react-native-elements';
import UserDC from '@/services/login/UserDC';
import FavoriteApiController from '@/services/apis/FavoriteApiController';
import Zone from '@/utils/Zone';
import {englishAge, englishTime} from '@/utils/commonParam';
import {StyleSheet} from 'react-native';

interface BulletinBlockProps {
  navigation: any;
  bulletin: any;
  refreshParent: () => void;
}

export default class BulletinBlock extends React.Component<
  BulletinBlockProps,
  {}
> {
  private async deleteFavoriteBulletin(item: any) {
    try {
      await FavoriteApiController.deleteFavoriteBulletin({
        bulletinId: item.bulletinId,
      });
      this.props.refreshParent();
    } catch (e) {
      console.error(e);
    }
  }

  render() {
    return (
      <TouchableOpacity
        style={this.styles.card}
        onPress={() =>
          this.props.navigation.navigate('bulletinDash', {
            bulletinId: this.props.bulletin.bulletinId,
          })
        }>
        <View style={this.styles.imageContainer}>
          <Image
            style={this.styles.cardImage}
            source={{uri: this.props.bulletin.thumbnailImageFileUrl}}
          />
          <View style={this.styles.zoneContainer}>
            <Icon name="location-sharp" size={12} color="#FFF" />
            <Text style={this.styles.zone}>
              {Zone.getZone(this.props.bulletin.zoneId)[1]}
            </Text>
          </View>
        </View>
        <View style={this.styles.cardContent}>
          <Text style={this.styles.title}>{this.props.bulletin.title}</Text>
          <Text style={this.styles.age}>
            {englishAge[this.props.bulletin.age]} ·{' '}
            {this.props.bulletin.timeType
              .split('|')
              .map((time: string) => englishTime[time])
              .join('/')}
          </Text>
          <View style={this.styles.personContainer}>
            <Icon name="person" size={14} color="#4f6cff" />
            <Text style={this.styles.peopleBold}>
              {this.props.bulletin.currentPeople}
            </Text>
            <Text style={this.styles.people}>
              /{this.props.bulletin.maxPeople}
            </Text>
          </View>
        </View>

        <View style={this.styles.buttonContainer}>
          <Button
            icon={<Icon name="heart" size={30} color="#ff4f4f" />}
            buttonStyle={this.styles.button}
            onPress={() => {
              if (UserDC.isLogout()) {
                Alert.alert('로그인 해주세요.');
                return;
              }
              this.deleteFavoriteBulletin(this.props.bulletin);
            }}
          />
        </View>
      </TouchableOpacity>
    );
  }

  private styles = StyleSheet.create({
    card: {
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
    zoneContainer: {
      position: 'absolute',
      bottom: 0,
      flexDirection: 'row',
      borderRadius: 9,
      backgroundColor: 'rgba(0, 0, 0, 0.3)',
      padding: 2,
      paddingHorizontal: 5,
      margin: 8,
    },
    button: {
      backgroundColor: 'transparent',
    },
    personContainer: {flexDirection: 'row', alignItems: 'center'},
    buttonContainer: {position: 'absolute', right: 0},
  });
}
